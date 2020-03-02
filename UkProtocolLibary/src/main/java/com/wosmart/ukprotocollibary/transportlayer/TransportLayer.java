package com.wosmart.ukprotocollibary.transportlayer;

import android.content.Context;
import android.os.Handler;

import com.realsil.realteksdk.logger.ZLogger;
import com.realsil.realteksdk.utility.DataConverter;
import com.wosmart.ukprotocollibary.gattlayer.GattLayer;
import com.wosmart.ukprotocollibary.gattlayer.GattLayerCallback;

import java.util.ArrayList;


public class TransportLayer {
	// Log
	private final static String TAG = "TransportLayer";
	private final static boolean D = true;
	
	// state control
	private ArrayList<byte[]> mTxPacketList;

	private volatile Integer mTxState = TX_STATE_IDLE;
	private final static int TX_STATE_IDLE = 0;
	private final static int TX_STATE_IN_TX = 1;
	private final static int TX_STATE_IN_SEND_ACK = 2;

	private TransportLayerPacket mCurrentRxPacket;

	private ArrayList<TransportLayerPacket> mRxPacketList;
	private volatile Integer mRxState = RX_STATE_HEADER;
	private final static int RX_STATE_HEADER = 0;
	private final static int RX_STATE_DATA = 1;
	
	// use to manager current transport layer sequence
	private volatile int mCurrentTxSequenceId;
	private volatile int mLastRxSuquenceId;
	
	// use to manager packet send
	private volatile boolean isAckCome;
	private volatile boolean isSentAckRight;
	private final Object mAckLock = new Object();
	
	// retransmit control.
	private int mRetransCounter;
	private final static int MAX_RETRANSPORT_COUNT = 3;
	
	// Thread for unpack send
	private ThreadTx mThreadTx;
	private ThreadRx mThreadRx;
	private static int MTU_PAYLOAD_SIZE_LIMIT = 20;
	
	// Use to manager data send
	private boolean isDataSend;
	private final Object mSendDataLock = new Object();
	private final int MAX_DATA_SEND_WAIT_TIME = 10000;
	
	// Transport Layer Call
	private TransportLayerCallback mCallback;
	
	// Gatt Layer
	private GattLayer mGattLayer;

	
	public TransportLayer(Context context, TransportLayerCallback callback) {
		ZLogger.d(D, "initial");
		// register callback
		mCallback = callback;
				
		// initial receive buffer
		mTxPacketList = new ArrayList<>();
		mRxPacketList = new ArrayList<>();

		mCurrentRxPacket = new TransportLayerPacket();
		
		// initial the gatt layer
		mGattLayer = new GattLayer(context, mGattCallback);
	}

	/**
	 * Connect to the remote device.
	 * <p>This is an asynchronous operation. Once the operation has been completed, the
	 * {@link TransportLayerCallback#onConnectionStateChange} callback is invoked, reporting the result of the operation.
	 *
	 * @return the operation result
	 *
	 * */
	public boolean connect(String addr) {
		mCurrentTxSequenceId = 1;
		mRetransCounter = 0;
		mLastRxSuquenceId = -1;

		// initial state
		initialState();

		startTxSchedule();
		startRxSchedule();

		return mGattLayer.connect(addr);
	}

	private void initialState() {
		initialTxState();
		initialRxState();
	}

	private void initialTxState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IDLE;
		}
	}

	private boolean checkTxStateInTx() {
		boolean status = false;
		synchronized (mTxState) {
			status = (mTxState != TX_STATE_IDLE);
		}
		return status;
	}

	private void changeToTxDataState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IN_TX;
		}
	}

	private void changeToTxAckState() {
		synchronized (mTxState) {
			mTxState = TX_STATE_IN_SEND_ACK;
		}
	}


	private void initialRxState() {
		synchronized (mRxState) {
			mRxState = RX_STATE_HEADER;
		}
	}

	private boolean checkRxStateInReceiveHeaderMode() {
		boolean status = false;
		synchronized (mRxState) {
			status = mRxState == RX_STATE_HEADER;
		}
		return status;
	}

	private void changeToRxDataState() {
		synchronized (mRxState) {
			mRxState = RX_STATE_DATA;
		}
	}

	private void addToTxPacketList(byte[] packet) {
		synchronized (mTxPacketList) {
			mTxPacketList.add(packet);
			mTxPacketList.notifyAll();
		}
	}

	private byte[] getFromTxPacketList() {
		byte[] packet = null;
		synchronized (mTxPacketList) {
			if(mTxPacketList.size() > 0) {
				packet = mTxPacketList.remove(0);
			}else{
				try {
					mTxPacketList.wait(1*1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		return packet;
	}

	private int getTxPacketListSize() {
		synchronized (mTxPacketList) {
			return mTxPacketList.size();
		}
	}

	private void addToRxPacketList(TransportLayerPacket packet) {
		synchronized (mRxPacketList) {
			mRxPacketList.add(packet);
			mRxPacketList.notifyAll();
		}
	}

	private TransportLayerPacket getFromRxPacketList() {
		TransportLayerPacket packet = null;
		synchronized (mRxPacketList) {
			if(mRxPacketList.size() > 0) {
				packet = mRxPacketList.remove(0);
			} else {
				try {
					mRxPacketList.wait(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return packet;
	}
	/**
	 * Close, it will disconnect to the remote.
	 *
	 * @return the operation result
	 *
	 * */
	public void close() {
		ZLogger.d(D, "close()");
		// clear all the wait time.
		stopRxSchedule();
		stopTxSchedule();

		stopRxTimer();
		mGattLayer.close();
	}

	/**
	 * Disconnect, it will disconnect to the remote.
	 *
	 *
	 * */
	public void disconnect() {
		// clear all the wait time.
		stopRxSchedule();
		stopTxSchedule();
		stopRxTimer();
		mGattLayer.disconnectGatt();
	}
	/**
	 * Set the name
	 *
	 * @param name 		the name
	 */
	public void setDeviceName(String name) {
		ZLogger.d(D, "name: " + name);
		mGattLayer.setDeviceName(name);
	}
	/**
	 * Get the name
	 *
	 */
	public void getDeviceName() {
		ZLogger.d(D, "getDeviceName");
		mGattLayer.getDeviceName();
	}


	/**
	 * When the Low Layer receive a packet, it will call this method
	 * 
	 * @param data the receive data
	 * */
	public void receiveData(byte[] data) {
		decodeReceiveData(data);
	}
	
	/**
	 * Send Data packet to the remote. Up stack can use this method to send data.
	 * If last packet didn't send ok, didn't allow send next packet. 
	 * 
	 * @param data the send data
	 * 
	 * */
	public boolean sendData(byte[] data){

		// generate a data packet
		byte[] sendPacket = TransportLayerPacket.prepareDataPacket(data, mCurrentTxSequenceId);

		// Pending to tx list.
		addToTxPacketList(sendPacket);
		
		return true;
	}

	/**
	 * In callback, maybe it will do lot of thing, we make let it work in a thread.
	 * */
	private void tellUpstackPacketSend(final byte[] sendData, final boolean sendOK) {
		if(TransportLayerPacket.checkIsAckPacket(sendData)) {
			ZLogger.d(D, "is ack packet, don't need tell up stack.");
			return;
		}
		ZLogger.d(D, "sendOK: " + sendOK + ", sendData: " + DataConverter.bytes2Hex(sendData));

		// update tx sequence id
		mCurrentTxSequenceId ++;

		final byte[] appData = new byte[sendData.length - TransportLayerPacket.HEADER_LENGTH];
		System.arraycopy(sendData, TransportLayerPacket.HEADER_LENGTH, appData, 0, sendData.length - TransportLayerPacket.HEADER_LENGTH);

		mCallback.onDataSend(sendOK, appData);
	}
	
	/**
	 * Save the receive data to receive buffer. when state is normal or rx.
	 * 
	 * @param data receive data
	 * 
	 * */
	private void decodeReceiveData(byte[] data) {
		int result;
		// start rx timer
		startRxTimer();

		// Check parse header or data
		if(checkRxStateInReceiveHeaderMode()) {
			// change the state
			changeToRxDataState();
			ZLogger.d(D, "parse header.");
			// parse the header
			result = mCurrentRxPacket.parseHeader(data);
		} else {
			ZLogger.d(D, "parse data.");
			// parse the header
			result = mCurrentRxPacket.parseData(data);
		}

		// stop rx timer
		if(result != TransportLayerPacket.LT_SUCCESS) {
			stopRxTimer();
		}
		switch(result) {
			// Receive ACK
			case TransportLayerPacket.LT_ERROR_ACK:
				isSentAckRight = false;
			case TransportLayerPacket.LT_SUCCESS_ACK:
				isSentAckRight = true;
				// update state
				initialRxState();
				// Update tx status
				synchronized (mAckLock) {
					isAckCome = true;
					ZLogger.i(D, "<<<--- Receive ack, ack flag: " + isSentAckRight);
					mAckLock.notifyAll();
				}
				break;
			case TransportLayerPacket.LT_FULL_PACKET:
				// initial Rx state
				initialRxState();
				ZLogger.i(D, "<<<--- Receive a full packet, packet real payload: " + DataConverter.bytes2Hex(mCurrentRxPacket.getRealPayload()));

				// check whether a retransmit packet
				if(mCurrentRxPacket.getSequenceId() == mLastRxSuquenceId) {
					ZLogger.w(D, "<<<--- Maybe a retrans packet, send success ack");
					// send success ack to remote
					sendAckPacket(mCurrentRxPacket.getSequenceId(), false);
					return;
				}
				// update the last sequence id
				mLastRxSuquenceId = mCurrentRxPacket.getSequenceId();

				TransportLayerPacket packet = mCurrentRxPacket;

				// Need create a new packet
				mCurrentRxPacket = new TransportLayerPacket();

				// send success ack to remote
				sendAckPacket(packet.getSequenceId(), false);

				// Add the packet to rx list
				addToRxPacketList(packet);

				break;
			case TransportLayerPacket.LT_SUCCESS:
				// Only check in the end of a packet
				/*
				// check whether a retransmit packet
				if(mPacket.getSequenceId() == mLastRxSuquenceId) {
					ZLogger.d(D, "Receive a retransmit packet, mPacket.getSequenceId(): " + mPacket.getSequenceId() +
												", mLastRxSuquenceId: " + mLastRxSuquenceId);
					// update state
					mState = STATE_NORMAL;//State change must before ack send.
					// send success ack to remote
					sendAckPacketUseThread(false);
					return;
				}*/
				break;

			case TransportLayerPacket.LT_LENGTH_ERROR:
			case TransportLayerPacket.LT_CRC_ERROR:
				ZLogger.e(D, "<<<--- Some error when receive data, with result: " + result);
				// update state
				initialRxState();

				sendAckPacket(mCurrentRxPacket.getSequenceId(), true);
				break;
			case TransportLayerPacket.LT_MAGIC_ERROR:
				// if a magic error occur, just return
				ZLogger.e(D, "<<<--- Some error when receive data, with result: " + result);
				// update state
				initialRxState();
				break;
			default:
				ZLogger.e(D, "<<<--- Some error, with result: " + result);
				break;
		}
	}

	/**
	 * Send ACK packet to the remote.
	 *
	 * @param err error ack or a success ack
	 *
	 * */
	/*
	private void sendAckPacket(boolean err){
		ZLogger.e(D, "sendAckPacket, err: " + err);
		// generate a ack packet
		final byte[] sendByte = TransportLayerPacket.prepareAckPacket(err, mCurrentRxPacket.getSequenceId());

		if(sendByte == null) {
			ZLogger.e(D, "something error with null packet.");
			return;
		}

		addToTxPacketList(sendByte);
	}*/

	/**
	 * Send ACK packet to the remote.
	 *
	 * @param err error ack or a success ack
	 *
	 * */
	private void sendAckPacket(int id, boolean err){
		// generate a ack packet
		final byte[] sendByte = TransportLayerPacket.prepareAckPacket(err, id);

		if(sendByte == null) {
			ZLogger.e(D, "--->>> Something error in send ack packet, with null packet.");
			return;
		}

		//send it
		ZLogger.i(D, "pending to tx list, err: " + err + ", sendByte: " + DataConverter.bytes2Hex(sendByte));

		addToTxPacketList(sendByte);

		// protect tx ack packet
		changeToTxAckState();
		// send the data, here we do nothing while the data send error, we just think this operation will be done
		//sendGattLayerData(sendByte);
    }
	
	/**
	 * Send transport packet to gatt layer.
	 * <p>This is an synchronous operation. It will wait the {@link GattLayerCallback#onDataSend} callback is invoked.
	 * 
	 * @param data the send data
	 * 
	 * */
    private boolean sendGattLayerData(byte[] data) {
    	isDataSend = false;
		if(!mGattLayer.sendData(data)) {
			ZLogger.w(D, "sendGattLayerData error.");
			return false;
		}
		
		synchronized(mSendDataLock) {
			if(!isDataSend) {
				try {
					mSendDataLock.wait(MAX_DATA_SEND_WAIT_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return isDataSend;
    }


	public void startRxSchedule() {
		ZLogger.d(D, "startRxSchedule.");
		if(mThreadRx != null) {
			mThreadRx.StopRx();
		}

		mThreadRx = new ThreadRx();
		mThreadRx.start();
	}

	public void stopRxSchedule() {
		if(mThreadRx != null) {
			ZLogger.d(D, "stopRxSchedule.");
			mThreadRx.StopRx();
		}
	}

	public class ThreadRx extends Thread {

		private Boolean _stop = false;

		public void run() {
			ZLogger.d(D, "ThreadRx is run");

			TransportLayerPacket receiveData = null;

			while (true) {
				// Use to protect tx packet
				if(!checkTxStateInTx()) {
					if ((receiveData = getFromRxPacketList()) != null) {
						// tell up stack, send the packet to upstack
						int len = receiveData.getPayloadLength();
						byte[] rcv = new byte[len];
						if(len != 0) {
							System.arraycopy(receiveData.getRealPayload(), 0, rcv, 0, len);
						}
						// tell up stack
						//ZLogger.e(D, "tell up stack, receive full packet");

						mCallback.onDataReceive(rcv);
					}
				}

				synchronized (_stop) {
					if (_stop) {
						break;
					}
				}
			}

			ZLogger.d(D, "ThreadRx stop");
		}//run

		public void StopRx() {
			synchronized (_stop) {
				_stop = true;
			}
		}
	}


	public void startTxSchedule() {
		ZLogger.d(D, "startTxSchedule.");
		if(mThreadTx != null) {
			mThreadTx.StopTx();
		}

		mThreadTx = new ThreadTx();
		mThreadTx.start();
	}

	public void stopTxSchedule() {
		ZLogger.d(D, "stopTxSchedule.");
		if(mThreadTx != null) {
			mThreadTx.StopTx();
			synchronized(mAckLock) {
				isAckCome = false;
				isSentAckRight = false;
				mAckLock.notifyAll();
			}
		}
	}
	
	// unpack and send thread
    public class ThreadTx extends Thread {

		private Boolean _stop = false;

    	public void run() {
    		ZLogger.d(D, "ThreadTx is run");

			byte[] sendData = null;

			while (true) {

				if((sendData = getFromTxPacketList()) != null) {
					// Use to protect tx packet
					changeToTxDataState();

					mRetransCounter = 0;
					boolean packetSendStatus = false;
					packetSendStatus = UnpackSendPacket(sendData);
					// If the packet list have packet, just send it
					if(!packetSendStatus) {
						// check reach the max retrans time
						while(mRetransCounter < MAX_RETRANSPORT_COUNT) {
							synchronized (_stop) {
								if(_stop) {
									return;
								}
							}
							mRetransCounter++;

							ZLogger.w(D, "---> Retrans send it, mRetransCounter: " + mRetransCounter
									+ ", sendData: " + DataConverter.bytes2Hex(sendData)
									+ ", isAckCome: " + isAckCome
									+ ", isSentAckRight: " + isSentAckRight);
							// resend data
							packetSendStatus = UnpackSendPacket(sendData);

							if(packetSendStatus) {
								break;
							}
						}
					}

					// tell up stack.
					tellUpstackPacketSend(sendData, packetSendStatus);

					// Use to protect tx packet
					// Maybe need send ack.
					if(getTxPacketListSize() == 0) {
						initialTxState();
					}
				}
				synchronized (_stop) {
					if(_stop) {
						break;
					}
				}
			}

    		ZLogger.d(D, "ThreadTx stop");
    	}//run

		public void StopTx() {
			synchronized (_stop) {
				_stop = true;
			}
		}
    }

	private boolean UnpackSendPacket(byte[] data) {
		byte[] sendData = data;

		// send data to the remote device
		if(null != mGattLayer) {
			ZLogger.i(D, "---> data: " + DataConverter.bytes2Hex(sendData));

			// initial is ack come flag.
			isSentAckRight = false;
			isAckCome = false;
			// unpack the send data, because of the MTU size is limit
			int length = sendData.length;
			int unpackCount = 0;
			byte[] realSendData;
			do {

				if(length <= MTU_PAYLOAD_SIZE_LIMIT) {
					realSendData = new byte[length];
					System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, length);

					// update length value
					length = 0;
				} else {
					realSendData = new byte[MTU_PAYLOAD_SIZE_LIMIT];
					System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, MTU_PAYLOAD_SIZE_LIMIT);

					// update length value
					length = length - MTU_PAYLOAD_SIZE_LIMIT;
				}
				// send the data, here we do nothing while the data send error, we just think this operation will be done
				if(!sendGattLayerData(realSendData)) {
					ZLogger.e(D, "---> Send data error, may link is loss or gatt initial failed.");
					return false;
				}

				// unpack counter increase
				unpackCount++;

			} while(length != 0);
			// Check is a ACK packet or not
			if(TransportLayerPacket.checkIsAckPacket(sendData)) {
				return true;
			}
			// make sure ack be changed in receive ack
			synchronized(mAckLock) {
				if (!isAckCome) {
					// start wait ack timer
					try {
						mAckLock.wait(MAX_ACK_WAIT_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					return isSentAckRight;
				}
			}
		} else {//if(null != mGattLayer)
			return false;
		}

		return true;
	}


    // Ack super timer
    private final int MAX_ACK_WAIT_TIME = 5000;


    // Rx super timer
    private final int MAX_RX_WAIT_TIME = 30000;
	final Handler mRxHandler = new Handler();
	Runnable mRxSuperTask = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ZLogger.w(D, "Rx Packet Timeout");
			// update state
			initialRxState();

			// send error ack to remote
			sendAckPacket(mCurrentRxPacket.getSequenceId(), true);
			// stop timer
			stopRxTimer();
		}
	};
	private void startRxTimer(){
		ZLogger.d(D, "startRxTimer()");
		synchronized (mRxHandler) {
			mRxHandler.postDelayed(mRxSuperTask, MAX_RX_WAIT_TIME);
			//ZLogger.d(D, "mRxHandler.postDelayed");
		}
	}
	private void stopRxTimer() {
		ZLogger.d(D, "stopRxTimer()");
		synchronized (mRxHandler) {
			mRxHandler.removeCallbacks(mRxSuperTask);
			//ZLogger.d(D, "mRxHandler.removeCallbacks");
		}
	}
    
    GattLayerCallback mGattCallback = new GattLayerCallback() {
    	@Override
		public void onConnectionStateChange(final boolean status, final boolean newState) {
    		ZLogger.d(D, "status: " + status + ", newState: " + newState);
			mCallback.onConnectionStateChange(status, newState);
        }
		@Override
		public void onDataLengthChanged(final int length) {
			ZLogger.d(D, "length: " + length);

			MTU_PAYLOAD_SIZE_LIMIT = length;
		}
		@Override
    	public void onDataSend(final boolean status) {
    		ZLogger.d(D, "status: " + status);
    		synchronized(mSendDataLock) {
    			isDataSend = true;
    			mSendDataLock.notifyAll();
			}
        }
		@Override
		public void onDataReceive(final byte[] data) {
			ZLogger.d(D, "onDataReceive()");
			// be careful send ack may call by the GattCallback
			receiveData(data);
		}
		@Override
		public void onNameReceive(final String data) {
			mCallback.onNameReceive(data);
		}
	};
}

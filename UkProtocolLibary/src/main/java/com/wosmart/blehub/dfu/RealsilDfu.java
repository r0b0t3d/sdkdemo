package com.wosmart.blehub.dfu;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.realsil.realteksdk.blehub.dfu.IRealsilDfu;
import com.realsil.realteksdk.blehub.dfu.IRealsilDfuCallback;
import com.realsil.realteksdk.logger.ZLogger;

public class RealsilDfu {
    private static final String TAG = "RealsilDfu";
    private static final boolean DBG = true;


    private Context mContext;
    private RealsilDfuCallback mRealsilDfuCallback;
    private IRealsilDfu mService;
    private BluetoothAdapter mAdapter;

    /**
     * Create a RealsilDfu proxy object for interacting with the local
     * Realsil DFU service.
     */
    RealsilDfu(Context context, RealsilDfuCallback callback) {
        ZLogger.d(DBG, "RealsilDfu");
        mContext = context;
        mRealsilDfuCallback = callback;
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        doBind();

    }

    private boolean doBind() {
        ZLogger.d(DBG, "doBind");
        Intent intent = new Intent(mContext, DfuService.class);
        intent.setAction(IRealsilDfu.class.getName());
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        return true;
    }

    private void doUnbind() {
        ZLogger.d(DBG, "doUnbind");
        synchronized (mConnection) {
            if (mService != null) {
                try {
                    // unregister the callback
                    mService.unregisterCallback(RealsilDfu.class.getName(), mCallback);
                    mService = null;
                    mContext.unbindService(mConnection);
                } catch (Exception e) {
                    ZLogger.e(DBG, "Unable to unbind RealsilDfuService: " + e.toString());
                }
            }
        }
    }

    public void close() {
        ZLogger.d(DBG, "close");
        mRealsilDfuCallback = null;

        doUnbind();
    }

    public void finalize() {
        ZLogger.d(DBG, "finalize");
        close();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ZLogger.d(DBG, "Proxy object connected");
            mService = IRealsilDfu.Stub.asInterface(service);
            try {
                if (mService != null) {
                    mService.registerCallback(RealsilDfu.class.getName(), mCallback);
                }
            } catch (RemoteException e) {
            }
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onServiceConnectionStateChange(true, RealsilDfu.this);
            }
        }

        //onServiceDisconnected is only called in extreme situations (crashed / killed).
        public void onServiceDisconnected(ComponentName className) {
            ZLogger.d(DBG, "Proxy object disconnected with an extreme situations");
            try {
                if (mService != null) {
                    mService.unregisterCallback(RealsilDfu.class.getName(), mCallback);
                }
            } catch (RemoteException e) {
            }
            mService = null;
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onServiceConnectionStateChange(false, null);
                //mRealsilDfuCallback = null;
            }
        }
    };

    /**
     * Get the DFU proxy object.
     * <p>
     * <p>Clients must implement {@link RealsilDfuCallback} to get notified of
     * the connection status and to get the proxy object.
     *
     * @param context  Context of the application
     * @param listener The service Listener for connection callbacks.
     * @return true on success, false on error
     */
    public static boolean getDfuProxy(Context context, RealsilDfuCallback listener) {
        if (context == null || listener == null) return false;

        RealsilDfu dfu = new RealsilDfu(context, listener);
        return true;
    }

    private IRealsilDfuCallback.Stub mCallback = new IRealsilDfuCallback.Stub() {

        @Override
        public void onError(int e) throws RemoteException {
            ZLogger.e(DBG, String.valueOf(e));
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onError(e);
            }
        }

        @Override
        public void onSucess(int s) throws RemoteException {
            ZLogger.d(DBG, String.valueOf(s));
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onSucess(s);
            }
        }

        @Override
        public void onProcessStateChanged(int state) throws RemoteException {
            ZLogger.d(DBG, "state: " + state);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onProcessStateChanged(state);
            }
        }

        @Override
        public void onProgressChanged(int progress) throws RemoteException {
            ZLogger.d(DBG, "progress: " + progress);
            if (mRealsilDfuCallback != null) {
                mRealsilDfuCallback.onProgressChanged(progress);
            }
        }

    };

    /**
     * start OTA upgrade process, This method is ASYNCHRONOUS, it return true means start
     * OTA upgrade process, stop with the callback{@link RealsilDfuCallback}. if return false
     * means something error, please check the arguments and the bluetooth state.
     *
     * @param addr the device address which want to start OTA Upgrade
     * @param path the image file path
     * @return true/false  result of start, the
     */
    public boolean start(String addr, String path) {
        ZLogger.d(DBG, "start");
        if (mService != null && isEnabled()) {
            try {
                return mService.start(RealsilDfu.class.getName(), addr, path);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (mService == null) ZLogger.w(DBG, "Proxy not attached to service");
        if (!isEnabled()) ZLogger.w(DBG, "the bluetooth didn't on");
        return false;
    }

    private boolean isEnabled() {
        return mAdapter.getState() == BluetoothAdapter.STATE_ON;
    }

    /**
     * Sets AES secret key, this key's length must be 32
     *
     * @param key secret key
     * @return true/false  result of set key
     */
    public boolean setSecretKey(byte[] key) {
        ZLogger.d(DBG, "setSecretKey");
        if (mService != null) {
            try {
                return mService.setSecretKey(key);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * check version control, default is disable
     *
     * @param enable enable/disable the version check
     * @return true/false  	result of set version check
     */
    public boolean setVersionCheck(boolean enable) {
        ZLogger.d(DBG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.setVersionCheck(enable);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * check version control, default is disable
     *
     * @param enable enable/disable the version check
     * @return true/false  	result of set version check
     */
    public boolean setBatteryCheck(boolean enable) {
        ZLogger.d(DBG, "setBatteryCheck");
        if (mService != null) {
            try {
                return mService.setBatteryCheck(enable);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * get the current OTA process state{@link #STA_ORIGIN}
     *
     * @return state    result the current OTA process state
     */
    public int getCurrentOtaState() {
        ZLogger.d(DBG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.getCurrentOtaState();
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return -1;
    }

    /**
     * get the current OTA process start or not
     *
     * @return true/false  	true means is in OTA process
     */
    public boolean isWorking() {
        ZLogger.d(DBG, "setVersionCheck");
        if (mService != null) {
            try {
                return mService.isWorking();
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * get the current OTA work mode
     *
     * @return current work mode  	-1 means something error
     */
    public int getWorkMode() {
        ZLogger.d(DBG, "getWorkMode");
        if (mService != null) {
            try {
                return mService.getWorkMode();
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return -1;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return -1;
    }

    /**
     * set the current OTA work mode
     *
     * @return true/false  	true means set mode success
     */
    public boolean setWorkMode(int mode) {
        ZLogger.d(DBG, "setWorkMode");
        if (mService != null) {
            try {
                return mService.setWorkMode(mode);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * Set the current OTA image send speed, this only need use for Android4.4, for deal with the internal BUG.
     * The total speed should set though the current environment, if only have one connection(OTA), we may can
     * set 5000(5KB/s), if with a A2DP link, the speed suggest set with 1000(1KB/s). the normal speed is 3KB/s.
     * <p>
     * If work in silent mode, please set a min value, such as 100(100B/s) to make sure OTA process didn't affect
     * the normal traffic.
     *
     * @param en    enable or disable speed control
     * @param speed set the total speed
     * @return true/false  	true means set mode success
     */
    public boolean setSpeedControl(boolean en, int speed) {
        ZLogger.d(DBG, "setSpeedControl");
        if (mService != null) {
            try {
                return mService.setSpeedControl(en, speed);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * Set need wait user check flag, in silent mode, we will download image silent, but in the last,
     * when we do all the thing, we should wait user check to active the image.
     * <p>
     * Other work mode normally didn't use this.
     *
     * @param en enable or disable user check
     * @return true/false  	true means set mode success
     */
    public boolean setNeedWaitUserCheckFlag(boolean en) {
        ZLogger.d(DBG, "setNeedWaitUserCheckFlag");
        if (mService != null) {
            try {
                return mService.setNeedWaitUserCheckFlag(en);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }

    /**
     * When set the need wait user check flag, should call this method to active the image in the last
     * of OTA process.
     * <p>
     * Other work mode normally didn't use this.
     *
     * @param en enable or disable user check
     * @return true/false  	true means set mode success
     */
    public boolean activeImage(boolean en) {
        ZLogger.d(DBG, "activeImage");
        if (mService != null) {
            try {
                return mService.activeImage(en);
            } catch (RemoteException e) {
                ZLogger.e(DBG, "Stack:" + Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        ZLogger.w(DBG, "Proxy not attached to service");
        return false;
    }
}

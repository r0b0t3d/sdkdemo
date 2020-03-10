package com.wosmart.sdkdemo;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerEarMacPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerEarStatusPacket;

import java.lang.reflect.InvocationTargetException;

public class MacAddressActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "LoginActivity";

    private Toolbar toolbar;

    private Button btn_un_bond;

    private Button btn_request_address;

    private Button btn_band_connect;

    private Button btn_read_status;

    private String classicAddress = "";

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    String strPsw = "0000";

    private ConnectListener connectListener;

    private Handler handler;

    private ClassBtReceiver classBtReceiver;

    private IntentFilter filter;

    private BluetoothDevice remoteDevice;

    private BluetoothProfile.ServiceListener listener;

    private BluetoothHeadset mHeadset = null;

    private BluetoothA2dp mA2dp = null;

    private WristbandManagerCallback callback;

    private int headsetFlag = -1;

    private int a2dpFlag = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_address);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_un_bond = findViewById(R.id.btn_un_bond);
        btn_request_address = findViewById(R.id.btn_request_address);
        btn_band_connect = findViewById(R.id.btn_band_connect);
        btn_read_status = findViewById(R.id.btn_read_status);
    }

    private void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x01:
                        showToast("连接中");
                        break;
                    case 0x02:
                        if (headsetFlag == 1 && a2dpFlag == 1) {
                            showToast("连接成功");
                            dismissProgress();
                        } else if (headsetFlag == 2 || a2dpFlag == 2) {
                            showToast("连接失败");
                            dismissProgress();
                        }
                        break;
                    case 0x03:
                        showToast("连接失败");
                        dismissProgress();
                        break;
                }
            }
        };

        callback = new WristbandManagerCallback() {
            @Override
            public void onError(int error) {
                super.onError(error);
                Log.i(tag, getString(R.string.app_error));
            }

            @Override
            public void onEarMac(ApplicationLayerEarMacPacket packet) {
                super.onEarMac(packet);
                Log.i(tag, "address= " + packet.getMacAddress());
                classicAddress = packet.getMacAddress();
            }

            @Override
            public void onEarStatus(ApplicationLayerEarStatusPacket packet) {
                super.onEarStatus(packet);
                Log.i(tag, "status= " + packet.getPairState());
            }
        };

        connectListener = new ConnectListener() {
            @Override
            public void onStartConnect() {
                Log.i(tag, "start connect");
                handler.sendEmptyMessage(0x01);
            }

            @Override
            public void onConnecting() {
                Log.i(tag, " connecting");
                handler.sendEmptyMessage(0x01);
            }

            @Override
            public void onConnectSuccess() {
                Log.i(tag, "connect success");
                handler.sendEmptyMessage(0x02);
            }

            @Override
            public void onConnectFail() {
                Log.i(tag, "connect fail");
                handler.sendEmptyMessage(0x03);
            }
        };

        listener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceDisconnected(int profile) {
                Log.i(tag, "onServiceConnected ");
                if (profile == BluetoothProfile.HEADSET) {
                    mHeadset = null;
                } else if (profile == BluetoothProfile.A2DP) {
                    mA2dp = null;
                }
            }

            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                Log.i(tag, "onServiceConnected ");

                switch (profile) {
                    case BluetoothProfile.HEADSET:
                        mHeadset = (BluetoothHeadset) proxy;
                        try {
                            connectProfile(mHeadset, remoteDevice);
                            Log.i(tag, "connect file success 2");
                            headsetFlag = 1;
                            connectListener.onConnectSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(tag, "connect file fail 2");
                            headsetFlag = 2;
                            connectListener.onConnectFail();
                        }
                        break;
                    case BluetoothProfile.A2DP:
                        mA2dp = (BluetoothA2dp) proxy;
                        try {
                            connectProfile(mA2dp, remoteDevice);
                            Log.i(tag, "connect file success 1");
                            a2dpFlag = 1;
                            connectListener.onConnectSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(tag, "connect file fail 1");
                            a2dpFlag = 2;
                            connectListener.onConnectFail();
                        }
                        break;
                }
            }
        };

        WristbandManager.getInstance(this).registerCallback(callback);

        initReceiver();
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_un_bond.setOnClickListener(this);
        btn_request_address.setOnClickListener(this);
        btn_band_connect.setOnClickListener(this);
        btn_read_status.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_un_bond:
                unBond();
                break;
            case R.id.btn_band_connect:
                if (null != classicAddress && !classicAddress.isEmpty()) {
                    startScan();
                } else {
                    showToast("mac地址为空");
                }
                break;
            case R.id.btn_request_address:
                requestAddress();
                break;
            case R.id.btn_read_status:
                readStatus();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private synchronized void connectProfile(BluetoothProfile profile, BluetoothDevice device) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ClsUtils.connectProfile(profile, device);
    }

    public void unBond() {
        WristbandManager.getInstance(this).sendRemoveBondCommand();
        WristbandManager.getInstance(this).close();
        showToast("解绑成功,断开设备连接");
    }

    public void readStatus() {
        WristbandManager.getInstance(this).requestClassicStatus();
        showToast("发送成功");
    }

    private void initReceiver() {
        Log.i(tag, "register receiver");
        classBtReceiver = new ClassBtReceiver();
        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(classBtReceiver, filter);
    }

    private void unRegister() {
        Log.i(tag, "unRegister receiver");
        if (null != classBtReceiver) {
            unregisterReceiver(classBtReceiver);
        }
    }

    private synchronized void requestAddress() {
        Log.i(tag, "requestAddress");
        if (WristbandManager.getInstance(this).requestClassicAddress()) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void startScan() {
        Log.i(tag, "start scan");

        if (null == mBluetoothManager || null == mBluetoothAdapter) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        mBluetoothAdapter.startDiscovery();
    }

    private void stopScan() {
        Log.i(tag, "stop scan");
        if (null == mBluetoothManager || null == mBluetoothAdapter) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        mBluetoothAdapter.cancelDiscovery();
    }

    private boolean connect(final BluetoothDevice remoteDevice) {
        Log.i(tag, "connect");
        this.remoteDevice = remoteDevice;

        try {
            ClsUtils.setPin(remoteDevice.getClass(), remoteDevice, strPsw); // 手机和蓝牙采集的装置配对

            ClsUtils.createBond(remoteDevice.getClass(), remoteDevice);

            ClsUtils.cancelPairingUserInput(remoteDevice.getClass(), remoteDevice);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    private void connectProfile() {

        headsetFlag = -1;

        a2dpFlag = -1;

        connectListener.onStartConnect();

        mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.A2DP);

        mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.HEADSET);

        connectListener.onConnecting();
    }

    private class ClassBtReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (null != action && action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {

                Log.i(tag, "scan begin");
            } else if (null != action && action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

                Log.i(tag, "scan finish");
            } else if (null != action && action.equalsIgnoreCase(BluetoothDevice.ACTION_FOUND)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (null != device) {
                    String mac = device.getAddress();
//                    Log.i(tag, "classicAddress = " + classicAddress + "scan found mac = " + mac);
                    if (null != mac) {
                        if (classicAddress.equalsIgnoreCase(mac)) {
                            Log.i(tag, "scan same mac ");

                            stopScan();

                            int state = device.getBondState();

                            if (state == BluetoothDevice.BOND_NONE) {
                                if (connect(device)) {
                                    connectProfile();
                                } else {
                                    connectListener.onConnectFail();
                                }
                            } else {
                                MacAddressActivity.this.remoteDevice = device;
                                connectProfile();
                            }
                        }
                    }
                }
            }
        }
    }

    private interface ConnectListener {

        void onStartConnect();

        void onConnecting();

        void onConnectSuccess();

        void onConnectFail();
    }
}

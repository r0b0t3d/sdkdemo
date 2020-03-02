package com.wosmart.sdkdemo;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHealth;
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
import android.widget.EditText;

import com.wosmart.ClsUtils;
import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "LanguageActivity";

    private Toolbar toolbar;
    private Button btn_request_address;
    private EditText et_language;
    private Button btn_set;

    private String classicAddress = "";

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    String strPsw = "123456";

    private ConnectListener connectListener;

    private Handler handler;

    private ClassBtReceiver classBtReceiver;

    private IntentFilter filter;

    private BluetoothDevice remoteDevice;

    private BluetoothProfile.ServiceListener listener;

    private BluetoothHealth mHealth = null;

    private BluetoothHeadset mHeadset = null;

    private BluetoothA2dp mA2dp = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initView();
        initData();
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_request_address = findViewById(R.id.btn_request_address);
        et_language = findViewById(R.id.et_language);
        btn_set = findViewById(R.id.btn_set);
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
                        showToast("连接成功");
                        dismissProgress();
                        break;
                    case 0x03:
                        showToast("连接失败");
                        dismissProgress();
                        break;
                }
            }
        };

        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onClassAddress(String address) {
                super.onClassAddress(address);
                Log.i("LanguageActivity", address);
                classicAddress = address;

                startScan();
            }
        });


        connectListener = new LanguageActivity.ConnectListener() {
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
                            ClsUtils.connectProfile(mHeadset, remoteDevice);
                            Log.i(tag, "connect file success 2");
                            connectListener.onConnectSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(tag, "connect file fail 2");
                            connectListener.onConnectFail();
                        }
                        break;
                    case BluetoothProfile.A2DP:
                        mA2dp = (BluetoothA2dp) proxy;
                        try {
                            ClsUtils.connectProfile(mA2dp, remoteDevice);
                            Log.i(tag, "connect file success 1");
                            connectListener.onConnectSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i(tag, "connect file fail 1");
                            connectListener.onConnectFail();
                        }
                        break;
                }
            }
        };

        initReceiver();
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_request_address.setOnClickListener(this);
        btn_set.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_address:
                requestAddress();
                break;
            case R.id.btn_set:
                String languageStr = et_language.getText().toString();
                if (null != languageStr && !languageStr.isEmpty()) {
                    int language = Integer.parseInt(languageStr);
                    DeviceLanguage languageType;
                    if (language == 0) {
                        languageType = DeviceLanguage.LANGUAGE_SAMPLE_CHINESE;
                    } else if (language == 1) {
                        languageType = DeviceLanguage.LANGUAGE_TRADITIONAL_CHINESE;
                    } else if (language == 3) {
                        languageType = DeviceLanguage.LANGUAGE_SPANISH;
                    } else if (language == 4) {
                        languageType = DeviceLanguage.LANGUAGE_FRENCH;
                    } else if (language == 5) {
                        languageType = DeviceLanguage.LANGUAGE_GERMAN;
                    } else if (language == 6) {
                        languageType = DeviceLanguage.LANGUAGE_ITALIAN;
                    } else {
                        languageType = DeviceLanguage.LANGUAGE_ENGLISH;
                    }
                    setLanguage(languageType);
                } else {
                    showToast("请输入语言编号");
                }
                break;
        }
    }

    private void setLanguage(DeviceLanguage language) {
        if (WristbandManager.getInstance(this).settingLanguage(language)) {
            showToast("设置成功");
        } else {
            showToast("设置失败");
        }
    }

    private void requestAddress() {

        if (WristbandManager.getInstance(this).requestClassicAddress()) {
            showToast("发送成功");
        } else {
            showToast("发送失败");
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

    private void connect(final BluetoothDevice remoteDevice) {
        Log.i(tag, "connect");
        this.remoteDevice = remoteDevice;

        connectListener.onStartConnect();

        try {
            connectListener.onConnecting();

            ClsUtils.setPin(remoteDevice.getClass(), remoteDevice, strPsw); // 手机和蓝牙采集的装置配对

            ClsUtils.createBond(remoteDevice.getClass(), remoteDevice);

            ClsUtils.cancelPairingUserInput(remoteDevice.getClass(), remoteDevice);

            mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.A2DP);

            mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.HEADSET);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            connectListener.onConnectFail();
        }
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
                    Log.i(tag, "classicAddress = " + classicAddress + "scan found mac = " + mac);
                    if (null != mac) {
                        if (classicAddress.equalsIgnoreCase(mac)) {
                            Log.i(tag, "scan same mac ");

                            stopScan();

                            connect(device);
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

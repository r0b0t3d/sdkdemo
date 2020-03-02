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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wosmart.ClsUtils;
import com.wosmart.bandlibrary.bluetooth.search.SearchResult;
import com.wosmart.sdkdemo.Adapter.DeviceAdapter;
import com.wosmart.sdkdemo.Common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassBtActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "ClassBtActivity";

    private Toolbar toolbar;

    private TextView tv_address;

    private TextView tv_status;

    private Button btn_disconnect;

    private Button btn_un_bond;

    private Button btn_scan;

    private String address = "";

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private ClassBtReceiver classBtReceiver;

    private IntentFilter filter;

    private RecyclerView rcy_device;

    private List<SearchResult> devices;

    private DeviceAdapter adapter;

    private ConnectListener connectListener;

    String strPsw = "0000";

    private Handler handler;

    private BluetoothDevice remoteDevice;

    private BluetoothHealth mHealth = null;

    private BluetoothHeadset mHeadset = null;

    private BluetoothA2dp mA2dp = null;

    private boolean isScanning;

    private boolean isConnecting;

    private int a2dpFlag = -1;

    private int headsetFlag = -1;

    private BluetoothProfile.ServiceListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_bt);
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
        tv_address = findViewById(R.id.tv_address);
        tv_status = findViewById(R.id.tv_status);
        btn_disconnect = findViewById(R.id.btn_disconnect);
        btn_un_bond = findViewById(R.id.btn_un_bond);
        btn_scan = findViewById(R.id.btn_scan);
        rcy_device = findViewById(R.id.rcy_device);
    }

    private void initData() {
        tv_address.setText(address);

        btn_disconnect.setEnabled(false);

        btn_un_bond.setEnabled(false);

        initReceiver();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x01:
                        isConnecting = true;
                        tv_status.setText("连接中");
                        break;
                    case 0x02:
                        if (headsetFlag == 1 && a2dpFlag == 1) {
                            isConnecting = false;
                            btn_disconnect.setEnabled(true);
                            btn_un_bond.setEnabled(true);
                            showToast("连接成功");
                            dismissProgress();
                        } else if (headsetFlag == 2 || a2dpFlag == 2) {
                            isConnecting = false;
                            showToast("连接失败");
                            dismissProgress();
                        }
                        break;
                    case 0x03:
                        isConnecting = false;
                        tv_status.setText("连接失败");
                        dismissProgress();
                        break;
                }
            }
        };

        rcy_device.setLayoutManager(new LinearLayoutManager(this));
        devices = new ArrayList<>();
        adapter = new DeviceAdapter(this, devices);
        adapter.setListener(new DeviceAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if (!isConnecting) {
                    stopScan();
                    SearchResult result = devices.get(pos);
                    BluetoothDevice device = result.device;
                    int state = device.getBondState();
                    if (state == BluetoothDevice.BOND_NONE) {
                        ClassBtActivity.this.remoteDevice = device;
                        if (bind(device)) {
                            connect();
                        } else {
                            connectListener.onConnectFail();
                        }
                    } else {
                        ClassBtActivity.this.remoteDevice = device;
                        connect();
                    }
                } else {
                    showToast("正在连接中");
                }
            }
        });
        rcy_device.setAdapter(adapter);

        connectListener = new ClassBtActivity.ConnectListener() {
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
                Log.i(tag, "onServiceDisconnected ");
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
                            headsetFlag = 1;
                            connectListener.onConnectSuccess();
                            Log.i(tag, "connect file success 2");
                        } catch (Exception e) {
                            e.printStackTrace();
                            headsetFlag = 2;
                            Log.i(tag, "connect file fail 2");
                            connectListener.onConnectFail();
                        }
                        break;
                    case BluetoothProfile.A2DP:
                        mA2dp = (BluetoothA2dp) proxy;
                        try {
                            ClsUtils.connectProfile(mA2dp, remoteDevice);
                            a2dpFlag = 1;
                            connectListener.onConnectSuccess();
                            Log.i(tag, "connect file success 1");
                        } catch (Exception e) {
                            e.printStackTrace();
                            a2dpFlag = 2;
                            Log.i(tag, "connect file fail 1");
                            connectListener.onConnectFail();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
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

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_disconnect.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (isScanning) {
                    stopScan();
                } else {
                    startScan();
                }
                break;
            case R.id.btn_disconnect:

                break;
            case R.id.btn_un_bond:
                removeBond(remoteDevice.getClass(), remoteDevice);
                break;
        }
    }

    private void startScan() {
        Log.i(tag, "start scan");

        devices.clear();
        adapter.notifyDataSetChanged();

        if (null == mBluetoothManager || null == mBluetoothAdapter) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        mBluetoothAdapter.startDiscovery();
        isScanning = true;
    }

    private void stopScan() {
        Log.i(tag, "stop scan");
        if (null == mBluetoothManager || null == mBluetoothAdapter) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
        mBluetoothAdapter.cancelDiscovery();
        isScanning = false;
        tv_status.setText("扫描结束");
    }

    private boolean bind(final BluetoothDevice remoteDevice) {
        isConnecting = true;
        try {
            ClsUtils.setPin(remoteDevice.getClass(), remoteDevice, strPsw);

            ClsUtils.createBond(remoteDevice.getClass(), remoteDevice);

            ClsUtils.cancelPairingUserInput(remoteDevice.getClass(), remoteDevice);

            return true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    private void connect() {

        headsetFlag = -1;

        a2dpFlag = -1;

        isConnecting = true;

        connectListener.onStartConnect();

        mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.A2DP);

        mBluetoothAdapter.getProfileProxy(this, listener, BluetoothProfile.HEADSET);

        connectListener.onConnecting();
    }

    private void removeBond(Class btClass, BluetoothDevice device) {
        try {
            ClsUtils.removeBond(btClass, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ClassBtReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (null != action && action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                tv_status.setText("扫描开始");
                Log.i(tag, "scan begin");
            } else if (null != action && action.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                isScanning = false;
                tv_status.setText("扫描结束");
                Log.i(tag, "scan finish");
            } else if (null != action && action.equalsIgnoreCase(BluetoothDevice.ACTION_FOUND)) {
                tv_status.setText("发现设备");

                Log.i(tag, "scan found");

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                SearchResult result = new SearchResult(device);
                if (!devices.contains(result)) {
                    devices.add(result);
                    adapter.notifyDataSetChanged();
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

package com.wosmart.sdkdemo;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wosmart.bandlibrary.bluetooth.Code;
import com.wosmart.bandlibrary.bluetooth.connect.response.BleConnectResponse;
import com.wosmart.bandlibrary.bluetooth.connect.response.BleNotifyResponse;
import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.bluetooth.model.BleGattProfile;
import com.wosmart.bandlibrary.bluetooth.search.SearchResult;
import com.wosmart.bandlibrary.bluetooth.search.response.SearchResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.DfuListener;
import com.wosmart.bandlibrary.protocol.util.ByteUtil;
import com.wosmart.bandlibrary.upgrade.dfu.DfuProgressListener;
import com.wosmart.bandlibrary.upgrade.dfu.DfuServiceInitiator;
import com.wosmart.bandlibrary.upgrade.dfu.DfuServiceListenerHelper;
import com.wosmart.sdkdemo.Common.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class UpgradeActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private TextView tv_file_path;

    private Button btn_choose_file;

    private Button btn_start_upgrade;

    private ProgressDialog progressDialog;

    private Uri uri;

    private String path;

    private String mac;

    private String name;

    private boolean finded;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    disconnect(mac);
                    break;
                case 0x02:
                    String newMac = (String) msg.obj;
                    Log.i("upgrade2", "mac=" + newMac);
//                    connect(newMac);
                    scan(newMac);
//                    startUpgrade(mac, name, uri, path);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        getData();
        initView();
        initData();
        addListener();
    }

    private void getData() {
        Intent intent = getIntent();
        if (null != intent) {
            mac = intent.getStringExtra("mac");
            name = intent.getStringExtra("name");
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tv_file_path = findViewById(R.id.tv_file_path);
        btn_choose_file = findViewById(R.id.btn_choose_file);
        btn_start_upgrade = findViewById(R.id.btn_start_upgrade);
    }

    private void initData() {

    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_choose_file.setOnClickListener(this);
        btn_start_upgrade.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_file:
                chooseFile();
                break;
            case R.id.btn_start_upgrade:
                if (null != uri && null != path && !path.isEmpty()) {
                    if (null != name && !name.isEmpty() && null != mac && !mac.isEmpty()) {
//                        startUpgrade(mac, name, uri, path);
                        enterDfu(mac);
                    } else {
                        showToast("设备信息不正确");
                    }
                } else {
                    showToast("请选择升级包");
                }
                break;
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0x01);
    }

    private void enterDfu(final String mac) {
        WoBtOperationManager.getInstance(this).enterDFU(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new DfuListener() {
            @Override
            public void onEnterSuccess() {
                showToast("进入dfu成功");

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x01);
                    }
                }, 2000);
            }

            @Override
            public void onEnterFail() {
                showToast("进入dfu失败");
            }
        });
    }

    private void disconnect(final String mac) {
        WoBtOperationManager.getInstance(this).disConnect();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("upgrade1", "mac=" + mac);
                Message msg = new Message();
                msg.what = 0x02;
                msg.obj = ByteUtil.transformMac(mac);
                handler.sendMessage(msg);
            }
        }, 2000);
    }


    private void scan(final String mac) {
        finded = false;
        WoBtOperationManager.getInstance(this).startScan(new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (null != device) {
                    String address = device.getAddress();
                    if (null != address && !address.isEmpty()) {
                        if (address.equalsIgnoreCase(mac)) {
                            showToast("扫描成功");
                            finded = true;
                            connect(mac);
                            stopScan();
                        }
                    }
                }
            }

            @Override
            public void onSearchStopped() {
                if (!finded) {
                    showToast("未搜索到dfu设备");
                }
            }

            @Override
            public void onSearchCanceled() {
                if (!finded) {
                    showToast("未搜索到dfu设备");
                }
            }
        });
    }

    private void stopScan() {
        WoBtOperationManager.getInstance(this).stopScan();
    }


    private void connect(final String mac) {
        WoBtOperationManager.getInstance(this).connect(mac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if (code == Code.REQUEST_SUCCESS) {
                    showToast("重连成功");

                    startUpgrade(mac, name, uri, path);
                } else {
                    showToast("重连失败");
                }
            }
        }, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {

            }

            @Override
            public void onResponse(int code) {

            }
        });
    }

    /**
     * 开始升级
     *
     * @param mac  设备mac地址
     * @param name 设备名称
     * @param uri  升级包uri
     * @param path 升级包路径
     */
    private void startUpgrade(String mac, String name, Uri uri, String path) {
        DfuServiceInitiator starter = new DfuServiceInitiator(mac)
                .setDeviceName(name)
                .setKeepBond(true)
                .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true)
                .setDisableNotification(true)
                .setZip(uri, path);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            starter.createDfuNotificationChannel(this);
        }
        starter.start(this, DfuService.class);
        showProgress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01 && resultCode == RESULT_OK) {
            uri = data.getData();

            if (uri == null)
                return;
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();

            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(this, uri);
            }
            tv_file_path.setText(path);
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 根据uri 获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
    }

    private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            Log.i("dfu", "onDeviceConnecting");
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            Log.i("dfu", "onDeviceConnected");
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            Log.i("dfu", "onDfuProcessStarting");
            dismissProgress();
            showProgressDialog();
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            Log.i("dfu", "onDfuProcessStarted");
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            Log.i("dfu", "onEnablingDfuMode");
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            Log.i("dfu", "onProgressChanged" + percent);
            progressDialog.setProgress(percent);
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            Log.i("dfu", "onFirmwareValidating");
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            Log.i("dfu", "onDeviceDisconnecting");
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            Log.i("dfu", "onDeviceDisconnected");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            Log.i("dfu", "onDfuCompleted");
            stopDfu();
            dismiss();
            showToast("升级成功");
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            Log.i("dfu", "onDfuAborted");
            dismissProgress();
            dismiss();
            showToast("升级失败，请重新点击升级。");
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            Log.i("dfu", "onError");
            dismissProgress();
            stopDfu();
            dismiss();
            showToast("升级失败，请重新点击升级。");
        }
    };

    private void stopDfu() {
        Intent intent = new Intent(this, DfuService.class);
        stopService(intent);
    }

    private void showProgressDialog() {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在升级");
            progressDialog.setMessage("请稍等。。。");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void dismiss() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

}

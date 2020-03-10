package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class CameraActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "CameraActivity";
    private Toolbar toolbar;
    private Button btn_enter_camera;
    private Button btn_exit_camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initData();
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_enter_camera = findViewById(R.id.btn_enter_camera);
        btn_exit_camera = findViewById(R.id.btn_exit_camera);
    }

    private void initData() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onError(int error) {
                super.onError(error);
                Log.i(tag, getString(R.string.app_error));
            }

            @Override
            public void onTakePhotoRsp() {
                super.onTakePhotoRsp();
                Log.i(tag, getString(R.string.app_camera));
            }
        });
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_enter_camera.setOnClickListener(this);
        btn_exit_camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter_camera:
                enterCamera();
                break;
            case R.id.btn_exit_camera:
                exitCamera();
                break;
        }
    }

    private void enterCamera() {
        if (WristbandManager.getInstance(this).sendCameraControlCommand(true)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

    private void exitCamera() {
        if (WristbandManager.getInstance(this).sendCameraControlCommand(false)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }
}

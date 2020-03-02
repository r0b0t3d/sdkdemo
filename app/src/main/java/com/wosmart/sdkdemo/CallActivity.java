package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.bandlibrary.protocol.model.data.CallMessage;
import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class CallActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText et_phone;
    private EditText et_name;
    private Button btn_in_call;
    private Button btn_off_call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_phone = findViewById(R.id.et_phone);
        et_name = findViewById(R.id.et_name);
        btn_in_call = findViewById(R.id.btn_in_call);
        btn_off_call = findViewById(R.id.btn_off_call);

    }

    private void initData() {
        registerCall();
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_in_call.setOnClickListener(this);
        btn_off_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_in_call:
                String phoneNum = et_phone.getText().toString();
                String name = et_name.getText().toString();
                if (null != phoneNum && !phoneNum.isEmpty()) {
                    CallMessage callMessage = new CallMessage();
                    callMessage.setPhoneNum(phoneNum);
                    if (null != name && !name.isEmpty()) {
                        callMessage.setContractName(name);
                    }
                    inCall(callMessage);
                } else {
                    if (null != name && !name.isEmpty()) {
                        CallMessage callMessage = new CallMessage();
                        callMessage.setContractName(name);
                        inCall(callMessage);
                    } else {
                        showToast("请输入电话或昵称");
                    }
                }
                break;
            case R.id.btn_off_call:
                offCall();
                break;
        }
    }

    public void registerCall() {
        WristbandManager.getInstance(this).registerCallback(new WristbandManagerCallback() {
            @Override
            public void onError(int error) {
                super.onError(error);
                showToast("出错了");
            }

            @Override
            public void onEndCall() {
                super.onEndCall();
                showToast("来电拒接");
            }
        });
    }

    public void inCall(CallMessage message) {
        if (WristbandManager.getInstance(this).sendCallNotifyInfo(message.getPhoneNum())) {
            showToast("同步来电成功");
        } else {
            showToast("同步来电失败");
        }
    }

    private void offCall() {
        if (WristbandManager.getInstance(this).sendCallRejectNotifyInfo()) {
            showToast("挂断成功");
        } else {

            showToast("挂断失败");
        }
    }

}

package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.PwdListener;
import com.wosmart.bandlibrary.protocol.model.data.Time;
import com.wosmart.bandlibrary.protocol.model.enums.LanguageType;
import com.wosmart.bandlibrary.protocol.util.TimeUtil;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class PwdActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText et_pwd;
    private EditText et_language;
    private Button btn_check_pwd;
    private Button btn_check_pwd2;
    private EditText et_modify_pwd;
    private Button btn_modify_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_pwd = findViewById(R.id.et_pwd);
        et_language = findViewById(R.id.et_language);
        btn_check_pwd = findViewById(R.id.btn_check_pwd);
        btn_check_pwd2 = findViewById(R.id.btn_check_pwd2);
        et_modify_pwd = findViewById(R.id.et_modify_pwd);
        btn_modify_pwd = findViewById(R.id.btn_modify_pwd);
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
        btn_check_pwd.setOnClickListener(this);
        btn_check_pwd2.setOnClickListener(this);
        btn_modify_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_pwd:
                String pwd = et_pwd.getText().toString();
                if (null != pwd && !pwd.isEmpty()) {
                    checkPwd(pwd);
                } else {
                    showToast("请输入密码");
                }
                break;
            case R.id.btn_check_pwd2:
                String pwd2 = et_pwd.getText().toString();
                if (null != pwd2 && !pwd2.isEmpty()) {
                    String languageStr = et_language.getText().toString();
                    if (null != languageStr && !languageStr.isEmpty()) {
                        int language = Integer.parseInt(languageStr);
                        LanguageType languageType;
                        if (language == 1) {
                            languageType = LanguageType.LANGUAGE_ZH;
                        } else if (language == 2) {
                            languageType = LanguageType.LANGUAGE_EN;
                        } else if (language == 3) {
                            languageType = LanguageType.LANGUAGE_FR;
                        } else if (language == 4) {
                            languageType = LanguageType.LANGUAGE_DE;
                        } else if (language == 5) {
                            languageType = LanguageType.LANGUAGE_IT;
                        } else if (language == 6) {
                            languageType = LanguageType.LANGUAGE_JA;
                        } else if (language == 7) {
                            languageType = LanguageType.LANGUAGE_ES;
                        } else {
                            languageType = LanguageType.LANGUAGE_EN;
                        }
                        Time time = new Time();
                        time.setTime(TimeUtil.getUtcTime());
                        time.setTimeZone(TimeUtil.getTimeZone());
                        checkPwd2(pwd2, languageType, time);
                    } else {
                        showToast("请输入语言");
                    }
                } else {
                    showToast("请输入密码");
                }
                break;
            case R.id.btn_modify_pwd:
                String modify_pwd = et_modify_pwd.getText().toString();
                if (null != modify_pwd && !modify_pwd.isEmpty()) {
                    modifyPwd(modify_pwd);
                } else {
                    showToast("请输入要修改的密码");
                }
                break;
        }
    }

    private void checkPwd(String pwd) {
        WoBtOperationManager.getInstance(this).checkPwd(pwd, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new PwdListener() {
            @Override
            public void onFail() {
                showToast("校验密码失败");
            }

            @Override
            public void onSuccess(boolean flag) {
                showToast("校验密码成功 " + flag);
            }
        });
    }

    private void checkPwd2(String pwd, LanguageType languageType, Time time) {
        WoBtOperationManager.getInstance(this).checkPwd2(pwd, languageType, time, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new PwdListener() {
            @Override
            public void onFail() {
                showToast("校验密码失败");
            }

            @Override
            public void onSuccess(boolean flag) {
                showToast("校验密码成功 " + flag);
            }
        });
    }

    private void modifyPwd(String pwd) {
        WoBtOperationManager.getInstance(this).updatePwd(pwd, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new PwdListener() {
            @Override
            public void onFail() {
                showToast("设置密码失败");
            }

            @Override
            public void onSuccess(boolean flag) {
                showToast("设置密码成功 " + flag);
            }
        });
    }
}

package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {
    private String tag = "LanguageActivity";

    private Toolbar toolbar;
    private EditText et_language;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        et_language = findViewById(R.id.et_language);
        btn_set = findViewById(R.id.btn_set);
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
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    showToast(getString(R.string.app_language_hint));
                }
                break;
        }
    }

    private void setLanguage(DeviceLanguage language) {
        if (WristbandManager.getInstance(this).settingLanguage(language)) {
            showToast(getString(R.string.app_success));
        } else {
            showToast(getString(R.string.app_fail));
        }
    }

}

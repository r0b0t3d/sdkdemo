package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.SendComplexDataListener;
import com.wosmart.bandlibrary.protocol.model.data.WeatherInfo;
import com.wosmart.bandlibrary.protocol.model.enums.WeatherType;
import com.wosmart.sdkdemo.Common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button btn_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
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
                WeatherInfo weatherInfo = new WeatherInfo();
                weatherInfo.setWeatherType(WeatherType.SUNNY_DAY);
                List<Integer> temps = new ArrayList<>();
                temps.add(21);
                temps.add(22);
                temps.add(23);
                temps.add(24);
                temps.add(25);
                temps.add(26);
                temps.add(27);
                temps.add(28);
                temps.add(29);
                temps.add(30);
                temps.add(29);
                temps.add(28);
                temps.add(27);
                temps.add(26);
                temps.add(25);
                temps.add(24);
                temps.add(23);
                temps.add(22);
                temps.add(21);
                temps.add(20);
                temps.add(19);
                temps.add(18);
                temps.add(17);
                temps.add(16);
                weatherInfo.setTemperatures(temps);
                weatherInfo.setCity("深圳");
                setWeather(weatherInfo);
                break;
        }
    }

    private void setWeather(WeatherInfo weather) {
        WoBtOperationManager.getInstance(this).syncWeather(weather, new SendComplexDataListener() {
            @Override
            public void onSendFail() {
                showToast("同步天气失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("同步天气成功");
            }

            @Override
            public void onSendProgress(int curPackage, int allPackage) {

            }
        });
    }
}

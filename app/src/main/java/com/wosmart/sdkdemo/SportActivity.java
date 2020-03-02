package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wosmart.bandlibrary.bluetooth.connect.response.BleWriteResponse;
import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.QuerySportStatusListener;
import com.wosmart.bandlibrary.protocol.listener.QuerySupportSportModeListener;
import com.wosmart.bandlibrary.protocol.listener.SportDataListener;
import com.wosmart.bandlibrary.protocol.model.data.SportData;
import com.wosmart.bandlibrary.protocol.model.data.SportStatusData;
import com.wosmart.bandlibrary.protocol.model.data.SupportSportMode;
import com.wosmart.bandlibrary.protocol.model.enums.SportType;
import com.wosmart.sdkdemo.Common.BaseActivity;

public class SportActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btn_query_sport_model;

    private Button btn_query_sport_status;

    private EditText et_sport_mode;

    private Button btn_start_sport;

    private Button btn_stop_sport;

    private Button btn_read_sport_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btn_query_sport_model = findViewById(R.id.btn_query_sport_model);
        btn_query_sport_status = findViewById(R.id.btn_query_sport_status);
        et_sport_mode = findViewById(R.id.et_sport_mode);
        btn_start_sport = findViewById(R.id.btn_start_sport);
        btn_stop_sport = findViewById(R.id.btn_stop_sport);
        btn_read_sport_data = findViewById(R.id.btn_read_sport_data);
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
        btn_query_sport_model.setOnClickListener(this);
        btn_query_sport_status.setOnClickListener(this);
        btn_start_sport.setOnClickListener(this);
        btn_stop_sport.setOnClickListener(this);
        btn_read_sport_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query_sport_model:
                querySportModel();
                break;
            case R.id.btn_query_sport_status:
                querySportStatus();
                break;
            case R.id.btn_start_sport:
                String sportStr = et_sport_mode.getText().toString();
                if (null != sportStr) {
                    int sportType = Integer.parseInt(sportStr);
                    SportType sportType1;
                    if (sportType == 1) {
                        sportType1 = SportType.OUT_DOOR_RUNNING;
                    } else if (sportType == 2) {
                        sportType1 = SportType.OUT_DOOR_RIDING;
                    } else if (sportType == 3) {
                        sportType1 = SportType.OUT_DOOR_WALKING;
                    } else if (sportType == 4) {
                        sportType1 = SportType.IN_DOOR_RUNNING;
                    } else if (sportType == 5) {
                        sportType1 = SportType.INTERVAL_TRAINING;
                    } else if (sportType == 6) {
                        sportType1 = SportType.PLANK;
                    } else {
                        showToast("不支持的运动模式");
                        return;
                    }
                    startSport(sportType1);
                } else {
                    showToast("请输入运动模式");
                }
                break;
            case R.id.btn_stop_sport:
                String sportStr2 = et_sport_mode.getText().toString();
                if (null != sportStr2) {
                    int sportType = Integer.parseInt(sportStr2);
                    SportType sportType1;
                    if (sportType == 1) {
                        sportType1 = SportType.OUT_DOOR_RUNNING;
                    } else if (sportType == 2) {
                        sportType1 = SportType.OUT_DOOR_RIDING;
                    } else if (sportType == 3) {
                        sportType1 = SportType.OUT_DOOR_WALKING;
                    } else if (sportType == 4) {
                        sportType1 = SportType.IN_DOOR_RUNNING;
                    } else if (sportType == 5) {
                        sportType1 = SportType.INTERVAL_TRAINING;
                    } else if (sportType == 6) {
                        sportType1 = SportType.PLANK;
                    } else {
                        showToast("不支持的运动模式");
                        return;
                    }
                    stopSport(sportType1);
                } else {
                    showToast("请输入运动模式");
                }
                break;
            case R.id.btn_read_sport_data:
                readSportData();
                break;
        }
    }

    private void querySportModel() {
        WoBtOperationManager.getInstance(this).querySupportSportMode(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new QuerySupportSportModeListener() {
            @Override
            public void onQueryFail() {
                showToast("查询运动模式失败");
            }

            @Override
            public void onQuerySuccess(SupportSportMode sportMode) {
                showToast("查询运动模式成功 " + sportMode);
            }
        });
    }

    private void querySportStatus() {
        WoBtOperationManager.getInstance(this).querySportStatus(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new QuerySportStatusListener() {
            @Override
            public void onFail() {
                showToast("查询运动状态失败 ");
            }

            @Override
            public void OnSuccess(SportStatusData sportStatusData) {
                showToast("查询运动状态成功 " + sportStatusData.toString());
            }
        });
    }

    private void startSport(SportType sportType) {
        WoBtOperationManager.getInstance(this).startDeviceSport(sportType, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new QuerySportStatusListener() {
            @Override
            public void onFail() {
                showToast("开始运动失败 ");
            }

            @Override
            public void OnSuccess(SportStatusData sportStatusData) {
                showToast("开始运动成功 " + sportStatusData.toString());
            }
        });
    }

    private void stopSport(SportType sportType) {
        WoBtOperationManager.getInstance(this).stopDeviceSport(sportType, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new QuerySportStatusListener() {
            @Override
            public void onFail() {
                showToast("结束运动失败 ");
            }

            @Override
            public void OnSuccess(SportStatusData sportStatusData) {
                showToast("结束运动失败 " + sportStatusData.toString());
            }
        });
    }

    private void readSportData() {
        WoBtOperationManager.getInstance(this).syncSportData(new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        }, new SportDataListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(SportData sportData) {

            }

            @Override
            public void onReadCompleted() {

            }
        });
    }
}

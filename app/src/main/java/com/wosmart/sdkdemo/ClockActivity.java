package com.wosmart.sdkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.wosmart.bandlibrary.protocol.WoBtOperationManager;
import com.wosmart.bandlibrary.protocol.listener.SendComplexDataListener;
import com.wosmart.bandlibrary.protocol.model.data.Clock;
import com.wosmart.bandlibrary.protocol.model.data.ClockData;
import com.wosmart.bandlibrary.protocol.util.TimeUtil;
import com.wosmart.sdkdemo.Common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ClockActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private RadioGroup rg_status;

    private EditText et_hour;

    private EditText et_minute;

    private RadioGroup rg_repeat;

    private CheckBox cb_sunday;

    private CheckBox cb_monday;

    private CheckBox cb_tuesday;

    private CheckBox cb_wednesday;

    private CheckBox cb_thursday;

    private CheckBox cb_friday;

    private CheckBox cb_saturday;

    private EditText et_event_id;

    private EditText et_event_content;

    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initView();
        initData();
        addListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rg_status = findViewById(R.id.rg_status);
        et_hour = findViewById(R.id.et_hour);
        et_minute = findViewById(R.id.et_minute);
        rg_repeat = findViewById(R.id.rg_repeat);
        cb_sunday = findViewById(R.id.cb_sunday);
        cb_monday = findViewById(R.id.cb_monday);
        cb_tuesday = findViewById(R.id.cb_tuesday);
        cb_wednesday = findViewById(R.id.cb_wednesday);
        cb_thursday = findViewById(R.id.cb_thursday);
        cb_friday = findViewById(R.id.cb_friday);
        cb_saturday = findViewById(R.id.cb_saturday);
        et_event_id = findViewById(R.id.et_event_id);
        et_event_content = findViewById(R.id.et_event_content);
        btn_send = findViewById(R.id.btn_send);
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
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                boolean status = false;
                if (rg_status.getCheckedRadioButtonId() == R.id.rb_open) {
                    status = true;
                }
                String hourStr = et_hour.getText().toString();
                String minuteStr = et_minute.getText().toString();
                boolean repeat = false;
                if (rg_repeat.getCheckedRadioButtonId() == R.id.rb_open2) {
                    repeat = true;
                }
                boolean sundayFlag = cb_sunday.isChecked();
                boolean mondayFlag = cb_monday.isChecked();
                boolean tuesdayFlag = cb_tuesday.isChecked();
                boolean wednesdayFlag = cb_wednesday.isChecked();
                boolean thursdayFlag = cb_thursday.isChecked();
                boolean fridayFlag = cb_friday.isChecked();
                boolean saturdayFlag = cb_saturday.isChecked();
                String eventIDStr = et_event_id.getText().toString();
                String eventContentStr = et_event_content.getText().toString();
                if (null != hourStr && !hourStr.isEmpty()) {
                    if (null != minuteStr && !minuteStr.isEmpty()) {
                        int startHour = Integer.parseInt(hourStr);
                        int startMinute = Integer.parseInt(minuteStr);
                        if (repeat == true) {
                            if (sundayFlag || mondayFlag || tuesdayFlag || wednesdayFlag || thursdayFlag || fridayFlag || saturdayFlag) {
                                String repeatStr = "";
                                if (sundayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (mondayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (tuesdayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (wednesdayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (thursdayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (fridayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (saturdayFlag) {
                                    repeatStr += "1";
                                } else {
                                    repeatStr += "0";
                                }
                                if (null != eventIDStr && !eventIDStr.isEmpty()) {
                                    int eventId = Integer.parseInt(eventIDStr);
                                    Clock clock = new Clock();
                                    clock.setID(0);
                                    clock.setOpen(status);
                                    String date = TimeUtil.getTodayDate();
                                    String startTime;
                                    if (startHour > 9) {
                                        startTime = date + " " + startHour;
                                    } else {
                                        startTime = date + " 0" + startHour;
                                    }
                                    if (startMinute > 9) {
                                        startTime += ":" + startMinute;
                                    } else {
                                        startTime += ":0" + startMinute;
                                    }
                                    clock.setTime(TimeUtil.getTime(startTime));
                                    clock.setTimeZone(TimeUtil.getTimeZone());
                                    clock.setRepeat(true);
                                    clock.setRepeatStr(repeatStr);
                                    clock.setEventID(eventId);
                                    clock.setEventContent(eventContentStr);

                                    ClockData clockData = new ClockData();
                                    List<Clock> clocks = new ArrayList<>();
                                    clocks.add(clock);
                                    clockData.setClocks(clocks);
                                    setClock(clockData);
                                } else {
                                    //默认为0
                                    Clock clock = new Clock();
                                    clock.setID(0);
                                    clock.setOpen(status);
                                    String date = TimeUtil.getTodayDate();
                                    String startTime;
                                    if (startHour > 9) {
                                        startTime = date + " " + startHour;
                                    } else {
                                        startTime = date + " 0" + startHour;
                                    }
                                    if (startMinute > 9) {
                                        startTime += ":" + startMinute;
                                    } else {
                                        startTime += ":0" + startMinute;
                                    }
                                    clock.setTime(TimeUtil.getTime(startTime));
                                    clock.setTimeZone(TimeUtil.getTimeZone());
                                    clock.setRepeat(true);
                                    clock.setRepeatStr(repeatStr);
                                    clock.setEventID(0);
                                    clock.setEventContent("");

                                    ClockData clockData = new ClockData();
                                    List<Clock> clocks = new ArrayList<>();
                                    clocks.add(clock);
                                    clockData.setClocks(clocks);
                                    setClock(clockData);
                                }
                            } else {
                                showToast("请至少选中一个要重复的日期");
                            }
                        } else {
                            if (null != eventIDStr && !eventIDStr.isEmpty()) {
                                int eventId = Integer.parseInt(eventIDStr);
                                Clock clock = new Clock();
                                clock.setID(0);
                                clock.setOpen(status);
                                String date = TimeUtil.getTodayDate();
                                String startTime;
                                if (startHour > 9) {
                                    startTime = date + " " + startHour;
                                } else {
                                    startTime = date + " 0" + startHour;
                                }
                                if (startMinute > 9) {
                                    startTime += ":" + startMinute;
                                } else {
                                    startTime += ":0" + startMinute;
                                }
                                clock.setTime(TimeUtil.getTime(startTime));
                                clock.setTimeZone(TimeUtil.getTimeZone());
                                clock.setRepeat(false);
                                clock.setRepeatStr("0000000");
                                clock.setEventID(eventId);
                                clock.setEventContent(eventContentStr);

                                ClockData clockData = new ClockData();
                                List<Clock> clocks = new ArrayList<>();
                                clocks.add(clock);
                                clockData.setClocks(clocks);
                                setClock(clockData);
                            } else {
                                //默认为0
                                Clock clock = new Clock();
                                clock.setID(0);
                                clock.setOpen(status);
                                String date = TimeUtil.getTodayDate();
                                String startTime;
                                if (startHour > 9) {
                                    startTime = date + " " + startHour;
                                } else {
                                    startTime = date + " 0" + startHour;
                                }
                                if (startMinute > 9) {
                                    startTime += ":" + startMinute;
                                } else {
                                    startTime += ":0" + startMinute;
                                }
                                clock.setTime(TimeUtil.getTime(startTime));
                                clock.setTimeZone(TimeUtil.getTimeZone());
                                clock.setRepeat(false);
                                clock.setRepeatStr("0000000");
                                clock.setEventID(0);
                                clock.setEventContent("");

                                ClockData clockData = new ClockData();
                                List<Clock> clocks = new ArrayList<>();
                                clocks.add(clock);
                                clockData.setClocks(clocks);
                                setClock(clockData);
                            }
                        }
                    } else {
                        showToast("请输入闹钟 分");
                    }
                } else {
                    showToast("请输入闹钟 时");
                }
                break;
        }
    }

    private void setClock(ClockData clockData) {
        WoBtOperationManager.getInstance(this).syncClock(clockData, new SendComplexDataListener() {
            @Override
            public void onSendFail() {
                showToast("同步闹钟失败");
            }

            @Override
            public void onSendSuccess() {
                showToast("同步闹钟成功");
            }

            @Override
            public void onSendProgress(int curPackage, int allPackage) {

            }
        });
    }
}

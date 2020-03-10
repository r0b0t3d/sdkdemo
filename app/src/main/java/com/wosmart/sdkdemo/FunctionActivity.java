package com.wosmart.sdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wosmart.sdkdemo.Adapter.FunctionAdapter;
import com.wosmart.sdkdemo.Common.BaseActivity;
import com.wosmart.sdkdemo.model.Function;

import java.util.ArrayList;
import java.util.List;

public class FunctionActivity extends BaseActivity {

    private Toolbar toolbar;

    private RecyclerView rcy_function;

    private List<Function> functions;

    private FunctionAdapter adapter;

    private String mac = "";

    private String name = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
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
        rcy_function = findViewById(R.id.rcy_function);
    }

    private void initData() {
        functions = new ArrayList<>();

        Function login = new Function(0, getString(R.string.app_login));

        Function time = new Function(1, getString(R.string.app_time));

        Function user = new Function(3, getString(R.string.app_user));

        Function clock = new Function(4, getString(R.string.app_clock));

        Function call = new Function(5, getString(R.string.app_call));

        Function notify = new Function(6, getString(R.string.app_notify));

        Function camera = new Function(7, getString(R.string.app_camera));

        Function sedentary = new Function(8, getString(R.string.app_sedentary));

        Function ui = new Function(9, getString(R.string.app_home_ui));

        Function disturb = new Function(10, getString(R.string.app_disturb));

        Function findBand = new Function(11, getString(R.string.app_find_band));

        Function findPhone = new Function(12, getString(R.string.app_find_phone));

        Function queryBattery = new Function(13, getString(R.string.app_battery));

        Function querySportMode = new Function(14, getString(R.string.app_sport));

        Function reset = new Function(15, getString(R.string.app_reset));

        Function step = new Function(18, getString(R.string.app_step));

        Function sleep = new Function(19, getString(R.string.app_sleep));

        Function rate = new Function(20, getString(R.string.app_rate));

        Function software = new Function(21, getString(R.string.app_software_upgrade));

        Function language = new Function(23, getString(R.string.app_language));

        Function unit = new Function(24, getString(R.string.app_unit));

        Function rateDetect = new Function(26, getString(R.string.app_rate_detect));

        Function lightControl = new Function(27, getString(R.string.app_light_control));

        Function notifySwitch = new Function(28, getString(R.string.app_notify_switch));

        Function device = new Function(29, getString(R.string.app_device_info));

        Function timer = new Function(32, getString(R.string.app_timer));

        Function stepGoal = new Function(33, getString(R.string.app_goal_setting));

        Function bp = new Function(34, getString(R.string.app_bp));

        Function syncData = new Function(35, getString(R.string.app_sync_data));

        Function macAddress = new Function(36, getString(R.string.app_mac_address));

        functions.add(login);
        functions.add(device);
        functions.add(time);
        functions.add(user);
        functions.add(language);
        functions.add(unit);
        functions.add(stepGoal);
        functions.add(syncData);
        functions.add(step);
        functions.add(sleep);
        functions.add(rate);
        functions.add(bp);
        functions.add(querySportMode);
        functions.add(queryBattery);
        functions.add(notifySwitch);
        functions.add(call);
        functions.add(notify);
        functions.add(clock);
        functions.add(camera);
        functions.add(rateDetect);
        functions.add(sedentary);
        functions.add(lightControl);
        functions.add(timer);
        functions.add(ui);
        functions.add(disturb);
        functions.add(findBand);
        functions.add(findPhone);
        functions.add(reset);
        functions.add(macAddress);
//        functions.add(software);

        rcy_function.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FunctionAdapter(this, functions);
        rcy_function.setAdapter(adapter);
    }

    private void addListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setListener(new FunctionAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                switch (pos) {
                    case 0:
                        Intent login = new Intent();
                        login.setClass(FunctionActivity.this, LoginActivity.class);
                        startActivity(login);
                        break;
                    case 1:
                        Intent time = new Intent();
                        time.setClass(FunctionActivity.this, TimeActivity.class);
                        startActivity(time);
                        break;
                    case 3:
                        Intent user = new Intent();
                        user.setClass(FunctionActivity.this, UserActivity.class);
                        startActivity(user);
                        break;
                    case 4:
                        Intent clock = new Intent();
                        clock.setClass(FunctionActivity.this, ClockActivity.class);
                        startActivity(clock);
                        break;
                    case 5:
                        Intent call = new Intent();
                        call.setClass(FunctionActivity.this, CallActivity.class);
                        startActivity(call);
                        break;
                    case 6:
                        Intent notify = new Intent();
                        notify.setClass(FunctionActivity.this, NotifyActivity.class);
                        startActivity(notify);
                        break;
                    case 7:
                        Intent camera = new Intent();
                        camera.setClass(FunctionActivity.this, CameraActivity.class);
                        startActivity(camera);
                        break;
                    case 8:
                        Intent sedentary = new Intent();
                        sedentary.setClass(FunctionActivity.this, SedentaryActivity.class);
                        startActivity(sedentary);
                        break;
                    case 9:
                        Intent homeUI = new Intent();
                        homeUI.setClass(FunctionActivity.this, HomeUiActivity.class);
                        startActivity(homeUI);
                        break;
                    case 10:
                        Intent disturb = new Intent();
                        disturb.setClass(FunctionActivity.this, DisturbActivity.class);
                        startActivity(disturb);
                        break;
                    case 11:
                        Intent findBand = new Intent();
                        findBand.setClass(FunctionActivity.this, FindBandActivity.class);
                        startActivity(findBand);
                        break;
                    case 12:
                        Intent findPhone = new Intent();
                        findPhone.setClass(FunctionActivity.this, FindPhoneActivity.class);
                        startActivity(findPhone);
                        break;
                    case 13:
                        Intent battery = new Intent();
                        battery.setClass(FunctionActivity.this, BatteryActivity.class);
                        startActivity(battery);
                        break;
                    case 14:
                        Intent sportModel = new Intent();
                        sportModel.setClass(FunctionActivity.this, SportActivity.class);
                        startActivity(sportModel);
                        break;
                    case 15:
                        Intent reset = new Intent();
                        reset.setClass(FunctionActivity.this, ResetActivity.class);
                        startActivity(reset);
                        break;
                    case 18:
                        Intent step = new Intent();
                        step.setClass(FunctionActivity.this, StepActivity.class);
                        startActivity(step);
                        break;
                    case 19:
                        Intent sleep = new Intent();
                        sleep.setClass(FunctionActivity.this, SleepActivity.class);
                        startActivity(sleep);
                        break;
                    case 20:
                        Intent rate = new Intent();
                        rate.setClass(FunctionActivity.this, HrActivity.class);
                        startActivity(rate);
                        break;
                    case 21:
                        Intent software = new Intent();
                        software.setClass(FunctionActivity.this, UpgradeActivity.class);
                        software.putExtra("mac", mac);
                        software.putExtra("name", name);
                        startActivity(software);
                        break;
                    case 23:
                        Intent language = new Intent();
                        language.setClass(FunctionActivity.this, LanguageActivity.class);
                        startActivity(language);
                        break;
                    case 24:
                        Intent unit = new Intent();
                        unit.setClass(FunctionActivity.this, UnitActivity.class);
                        startActivity(unit);
                        break;
                    case 26:
                        Intent rateDetect = new Intent();
                        rateDetect.setClass(FunctionActivity.this, HrDetectActivity.class);
                        startActivity(rateDetect);
                        break;
                    case 27:
                        Intent lightControl = new Intent();
                        lightControl.setClass(FunctionActivity.this, LightControlActivity.class);
                        startActivity(lightControl);
                        break;
                    case 28:
                        Intent notifySwitch = new Intent();
                        notifySwitch.setClass(FunctionActivity.this, NotifySwitchActivity.class);
                        startActivity(notifySwitch);
                        break;
                    case 29:
                        Intent device = new Intent();
                        device.setClass(FunctionActivity.this, DeviceInfoActivity.class);
                        startActivity(device);
                        break;
                    case 32:
                        Intent timer = new Intent();
                        timer.setClass(FunctionActivity.this, TimerActivity.class);
                        startActivity(timer);
                        break;
                    case 33:
                        Intent stepGoal = new Intent();
                        stepGoal.setClass(FunctionActivity.this, StepGoalActivity.class);
                        startActivity(stepGoal);
                        break;
                    case 34:
                        Intent bp = new Intent();
                        bp.setClass(FunctionActivity.this, BpActivity.class);
                        startActivity(bp);
                        break;
                    case 35:
                        Intent syncData = new Intent();
                        syncData.setClass(FunctionActivity.this, SyncDataActivity.class);
                        startActivity(syncData);
                        break;
                    case 36:
                        Intent macAddress = new Intent();
                        macAddress.setClass(FunctionActivity.this, MacAddressActivity.class);
                        startActivity(macAddress);
                        break;
                }
            }
        });
    }

}

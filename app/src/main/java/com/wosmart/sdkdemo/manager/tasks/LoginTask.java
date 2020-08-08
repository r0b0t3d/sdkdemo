package com.wosmart.sdkdemo.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public class LoginTask extends CommonTask {
    private static final String TAG = "LoginTask";

    public LoginTask(WristbandManager wristbandManager, Callback c) {
        super(wristbandManager, c);
    }

    @Override
    WristbandManagerCallback initWristbandManagerCallback() {
        return new WristbandManagerCallback() {
            @Override
            public void onLoginStateChange(int state) {
                super.onLoginStateChange(state);
                Log.e(TAG, "Login state " + state);
                if (state == WristbandManager.STATE_WRIST_LOGIN) {
                    Log.e(TAG, "Login success");
                    callback.onSuccess();
                }
            }
        };
    }

    @Override
    public void run() {
        super.run();
        wristbandManager.startLoginProcess("1234567890");
    }

}

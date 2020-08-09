package com.northroom.bhs.nodescanner.manager.tasks;

import android.util.Log;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginTask extends CommonTask {
    private static final String TAG = "LoginTask";
    private AtomicBoolean isLoggedIn = new AtomicBoolean(false);

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
                if (isInterrupted()) {
                    return;
                }
                if (state == WristbandManager.STATE_WRIST_LOGIN) {
                    Log.e(TAG, "Login success");
                    onSuccess();
                }
            }
        };
    }

    @Override
    public void run() {
        super.run();
        wristbandManager.startLoginProcess("1234567890");

        try {
            sleep(10 * 1000);
            if (!isLoggedIn.get()) {
                onFailed();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.wosmart.sdkdemo.manager.tasks;

import com.wosmart.ukprotocollibary.WristbandManager;

public abstract class CommonTask extends Thread {
    protected WristbandManager wristbandManager;
    protected Callback callback;

    public CommonTask(WristbandManager wristbandManager, Callback callback) {
        this.wristbandManager = wristbandManager;
        this.callback = callback;
    }

    public interface Callback {
        void onSuccess(Object ...args);
        void onFailed();
    }
}


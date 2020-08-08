package com.wosmart.sdkdemo.manager.tasks;

import com.wosmart.ukprotocollibary.WristbandManager;
import com.wosmart.ukprotocollibary.WristbandManagerCallback;

public abstract class CommonTask extends Thread {
    protected WristbandManager wristbandManager;
    protected WristbandManagerCallback wristbandManagerCallback;
    protected Callback callback;

    public CommonTask(WristbandManager wristbandManager, Callback callback) {
        this.wristbandManager = wristbandManager;
        this.callback = callback;

        wristbandManagerCallback = initWristbandManagerCallback();
        wristbandManager.registerCallback(wristbandManagerCallback);
    }

    abstract WristbandManagerCallback initWristbandManagerCallback();

    protected void onSuccess(Object ...params) {
        if (!isInterrupted()) {
            interrupt();
        }
        wristbandManager.unRegisterCallback(wristbandManagerCallback);
        callback.onSuccess(params);
    }

    protected void onFailed() {
        if (!isInterrupted()) {
            interrupt();
        }
        wristbandManager.unRegisterCallback(wristbandManagerCallback);
        callback.onFailed();
    }

    public interface Callback {
        void onSuccess(Object ...args);
        void onFailed();
    }
}


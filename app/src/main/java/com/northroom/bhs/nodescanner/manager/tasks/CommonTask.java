package com.northroom.bhs.nodescanner.manager.tasks;

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
        wristbandManager.unRegisterCallback(wristbandManagerCallback);
        callback.onSuccess(params);
        if (!isInterrupted()) {
            interrupt();
        }
    }

    protected void onFailed() {
        wristbandManager.unRegisterCallback(wristbandManagerCallback);
        callback.onFailed();
        if (!isInterrupted()) {
            interrupt();
        }
    }

    public interface Callback {
        void onSuccess(Object ...args);
        void onFailed();
    }
}


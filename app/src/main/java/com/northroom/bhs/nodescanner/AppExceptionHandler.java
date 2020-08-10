package com.northroom.bhs.nodescanner;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.northroom.bhs.nodescanner.activity.MainActivity;

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;
    public AppExceptionHandler(Activity a) {
        activity = a;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}

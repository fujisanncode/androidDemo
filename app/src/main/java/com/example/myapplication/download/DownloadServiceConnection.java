package com.example.myapplication.download;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class DownloadServiceConnection implements ServiceConnection {
    private DownloadBinder downloadBinder;

    public DownloadBinder getDownloadBinder() {
        return downloadBinder;
    }

    private static final String TAG = "DownloadService";

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: 活动连接到服务");
        this.downloadBinder = (DownloadBinder) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "onServiceDisconnected: 活动和服务断开");
    }
}

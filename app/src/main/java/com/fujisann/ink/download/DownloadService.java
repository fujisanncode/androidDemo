package com.fujisann.ink.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fujisann.ink.MainActivity;
import com.fujisann.ink.R;

public class DownloadService extends Service {

  private static final String TAG = "DownloadService";

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "服务创建");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand: 服务启动");
    return super.onStartCommand(intent, flags, startId);
  }

  private DownloadBinder downloadBinder;

  public DownloadBinder getDownloadBinder() {
    return downloadBinder;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    this.downloadBinder = new DownloadBinder(this);
    return this.downloadBinder;
  }

  /**
   * 获取服务的消息管理器，用于操作消息
   *
   * @return
   */
  private NotificationManager notificationManager;

  public NotificationManager getNotificationManager() {
    if (this.notificationManager == null) {
      this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    return this.notificationManager;
  }

  public static final String notification_channel_id = "channel_download";

  /**
   * 构建下载消息
   *
   * @param title 下载进度名称
   * @param progress 下载进度
   * @return
   */
  public Notification getNotification(String title, int progress) {
    Log.d(TAG, "getNotification: 更新下载进度" + Thread.currentThread().getName());
    // android8, 必须先注册channel， 然后使用channelId
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel notificationChannel =
          new NotificationChannel(
              notification_channel_id, "download channel", NotificationManager.IMPORTANCE_DEFAULT);
      ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
          .createNotificationChannel(notificationChannel);
    }

    Intent intent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(this, notification_channel_id)
            .setSmallIcon(R.mipmap.ic_launcher) // 小图
            .setLargeIcon(bitmap) // 大图转换
            .setContentTitle(title) // 标题
            .setContentIntent(pendingIntent); // 点击通知跳转到活动
    // 通知显示进度条
    if (progress > 0) {
      builder.setContentText(progress + "%");
      builder.setProgress(100, progress, false);
    }
    Log.d(TAG, "getNotification: 准备发送通知消息");
    return builder.build();
  }
}

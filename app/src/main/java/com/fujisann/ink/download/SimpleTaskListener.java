package com.fujisann.ink.download;

import android.util.Log;
import android.widget.Toast;

public class SimpleTaskListener implements TaskListener {

  private DownloadService downloadService;

  public SimpleTaskListener(DownloadService downloadService) {
    this.downloadService = downloadService;
  }

  private static final String TAG = "SimpleTaskListener";

  @Override
  public void onProgress(int progress) {
    Log.d(TAG, "onProgress: 准备更新下载进度" + Thread.currentThread().getName());
    // 在通知栏显示消息
    downloadService
        .getNotificationManager()
        .notify(1, downloadService.getNotification("更新下载进度...", progress));
  }

  @Override
  public void onCancel() {
    // 取消下载任务，取消任务的时候在final中已经删除了已经下载的文件了
    downloadService.getDownloadBinder().setDownloadTask(null);
    // 停止前台服务
    downloadService.stopForeground(true);
    Toast.makeText(this.downloadService, "下载任务和前台任务都取消了", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSuccess() {
    // 停止异步任务
    downloadService.getDownloadBinder().setDownloadTask(null);
    // 停止前台通知服务
    downloadService.stopForeground(true);
    // 推送下载成功的消息
    downloadService
        .getNotificationManager()
        .notify(1, downloadService.getNotification("下载成功", 100));
    Toast.makeText(downloadService, "下载成功，请检查通知消息", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onFail() {
    // 下载失败, 停止异步任务，停止前台服务，发送失败消息
    downloadService.getDownloadBinder().setDownloadTask(null);
    downloadService.stopForeground(true);
    downloadService.getNotificationManager().notify(1, downloadService.getNotification("下载失败", 0));
  }

  @Override
  public void onPause() {
    // 暂停仅停止异步任务，不停止前台服务
    downloadService.getDownloadBinder().setDownloadTask(null);
    Toast.makeText(downloadService, "暂停下载", Toast.LENGTH_SHORT).show();
  }
}

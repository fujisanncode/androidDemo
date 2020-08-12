package com.fujisann.ink.download;

import android.os.Binder;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

// 服务的绑定者
public class DownloadBinder extends Binder {

  private static final String TAG = "DownloadBinder";

  private DownloadService downloadService;

  public DownloadBinder(DownloadService service) {
    this.downloadService = service;
  }

  private DownloadTask downloadTask;

  public DownloadTask getDownloadTask() {
    return downloadTask;
  }

  public void setDownloadTask(DownloadTask downloadTask) {
    this.downloadTask = downloadTask;
  }

  private String downloadUrl;

  public void startDownload(String url) {
    // 只允许一个异步线程去执行下载任务
    if (this.downloadTask != null) {
      Toast.makeText(this.downloadService, "下载任务进行中，不要重复执行", Toast.LENGTH_SHORT).show();
      return;
    }
    this.downloadUrl = url;
    // 启动异步任务后，执行后台方法
    this.downloadTask = new DownloadTask(new SimpleTaskListener(this.downloadService));
    this.downloadTask.execute(url);
    // 启动前台服务（此时通知栏不显示消息，知道发送消息通知栏才会显示消息）
    // 通知的id是1, 后面通过此id操作通知
    downloadService.startForeground(1, downloadService.getNotification("启动下载任务...", 0));
    // Toast.makeText(downloadService, "前台服务已经就绪，等待下载进度消息推送到前台服务", Toast.LENGTH_SHORT).show();
  }

  /** 调用异步任务，停止下载 */
  public void cancelDownload() {
    if (this.downloadTask != null) {
      // 如果下载任务没有停止，则停止下载任务（停止的过程中已经删除了下载的文件）
      this.downloadTask.cancelTask();
    } else {
      // 如果下载任务已经停止，
      if (!TextUtils.isEmpty(this.downloadUrl)) {
        // 删除外部存储设备中文件, 则删除已经下载的文件，删除通知，停止前台服务
        String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getPath();
        File file = new File(path + fileName);
        if (file.exists()) {
          file.delete();
        }
        // 取消推送消息，停止前台服务
        downloadService.getNotificationManager().cancel(1);
        downloadService.stopForeground(true);
        Toast.makeText(downloadService, "不需要停止下载任务，仅停止前台任务，并且删除已经下载的文件", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void pauseDownload() {
    if (this.downloadTask != null) {
      this.downloadTask.pauseTask();
    }
  }
}

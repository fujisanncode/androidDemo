package com.fujisann.ink.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

  private static final String TAG = "DownloadTask";

  private TaskListener taskListener;

  public DownloadTask(TaskListener taskListener) {
    this.taskListener = taskListener;
  }

  /** 下载成功 */
  public static final int type_success = 0;
  /** 下载暂停 */
  public static final int type_pause = 1;
  /** 下载取消 */
  public static final int type_cancel = 2;
  /** 下载失败 */
  public static final int type_fail = 3;

  @Override
  protected Integer doInBackground(String... strings) {
    Log.d(TAG, "doInBackground: 异步启动下载");
    File file = null;
    InputStream inputStream = null;
    RandomAccessFile accessFile = null;
    Response response = null;
    try {
      OkHttpClient okHttpClient = new OkHttpClient();

      // 已经下载的长度，用于断点续传
      long downloadLength = 0;
      String downloadUrl = strings[0];

      // 在外部存储构建文件用来保存请求数据
      String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
      String downloadPath =
          Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
      // 构建逻辑文件对象(指向存在的文件或者拟新建的文件)
      file = new File(downloadPath + fileName);

      // 如果外部存储中已经存在文件(即已经下载部分)，读取下载部分长度
      if (file.exists()) {
        downloadLength = file.length();
      }

      // 下载文件的总长度
      long contentLength = getContentLength(downloadUrl);
      if (contentLength == downloadLength) {
        // 下载完毕
        return type_success;
      }

      if (contentLength == 0) {
        // 没有获取到文件长度，返回失败
        return type_fail;
      }

      // 执行文件下载
      Request request =
          new Request.Builder()
              // 断点续传
              .addHeader("RANGE", "bytes=" + downloadLength + "-")
              .url(downloadUrl)
              .build();
      Log.d(TAG, "doInBackground: 准备下载");
      long start = System.currentTimeMillis();
      response = okHttpClient.newCall(request).execute();
      Log.i(TAG, "getContentLength: " + (System.currentTimeMillis() - start) + "ms");

      if (response.body() != null) {
        inputStream = response.body().byteStream();
        // 写入到文件中，文件权限为rw
        accessFile = new RandomAccessFile(file, "rw");
        // 跳过已经下载的部分
        accessFile.seek(downloadLength);

        byte[] readBytes = new byte[1024];
        int length;
        // 统计本地下载累计长度
        long totalLen = 0;
        while ((length = inputStream.read(readBytes)) != -1) {
          if (is_cancel) {
            return type_cancel;
          } else if (is_pause) {
            return type_pause;
          }
          totalLen += length;
          accessFile.write(readBytes, 0, length);

          Log.d(TAG, "doInBackground: 下载的异步线程是" + Thread.currentThread().getName());
          // 每写一次文件推送一次进度给服务(异步线程触发onProgressUpdate方法) long -> long -> int
          // Log.d(TAG, "doInBackground: 准备推送下载进度，当前时间为：" + System.currentTimeMillis());
          publishProgress((int) ((totalLen + downloadLength) * 100 / contentLength));
        }

        // 下载正常返回，文件写入存储中，返回成功
        return type_success;
      }
    } catch (IOException e) {
      Log.d(TAG, "请求异常");
    } finally {
      try {
        // 关闭资源
        if (response != null) {
          response.close();
        }
        if (inputStream != null) {
          inputStream.close();
        }
        if (accessFile != null) {
          accessFile.close();
        }
        // 如果是取消下载，则删除存储中下载的文件
        if (is_cancel && file != null) {
          file.delete();
        }
      } catch (IOException e) {
        Log.d(TAG, "关闭文件流异常");
      }
    }
    return type_fail;
  }

  /**
   * 异步任务执行后，通过监听取消服务
   *
   * @param integer 当前任务的状态
   */
  @Override
  protected void onPostExecute(Integer integer) {
    super.onPostExecute(integer);
    switch (integer) {
      case type_cancel:
        taskListener.onCancel();
        break;
      case type_success:
        taskListener.onSuccess();
        break;
      case type_fail:
        taskListener.onFail();
        break;
      case type_pause:
        taskListener.onPause();
        break;
      default:
        break;
    }
  }

  /** 标记当前执行的任务为已取消 */
  public void cancelTask() {
    this.is_cancel = true;
  }

  public void pauseTask() {
    this.is_pause = true;
  }

  private int lastProgress;

  @Override
  protected void onProgressUpdate(Integer... values) {
    super.onProgressUpdate(values);
    Integer progress = values[0];
    // 判断需要更新则，发送通知消息，如果不判断，则因为通知消息发送过于频繁导致界面卡死（消息更新在main线程，过于频繁导致程序卡死）
    // 如果不判断，则没下载1k，更新一次进度；如果判断，则每下载1个百分点更新一次进度(更新频次降低很多)
    if (progress > this.lastProgress) {
      // 进度更新后，发送通知消息
      this.taskListener.onProgress(progress);
      this.lastProgress = progress;
    }
  }

  /** 当前为取消状态 */
  private boolean is_cancel = false;
  /** 当前为暂停状态 */
  private boolean is_pause = false;

  /**
   * 读取文件最大长度, 仅请求到数据长度即关闭连接
   *
   * @param url 文件下载url
   * @return 文件总长度
   */
  private long getContentLength(String url) {
    try {
      Request request = new Request.Builder().url(url).build();
      Log.d(TAG, "getContentLength: 获取下载文件大小");
      long start = System.currentTimeMillis();
      Response response = new OkHttpClient().newCall(request).execute();
      Log.i(TAG, "getContentLength: " + (System.currentTimeMillis() - start) + "ms");
      if (response.isSuccessful()) {
        long length = response.body().contentLength();
        response.close();
        return length;
      }
    } catch (IOException e) {
      Log.d(TAG, "getContentLength: 请求下载接口失败");
    }
    return 0;
  }
}

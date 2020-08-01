package com.example.myapplication.download;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseActivity;

import java.io.File;

public class DownloadActivity extends BaseActivity implements View.OnClickListener {

  // 下载tomcat
  public static final String down_url =
      "https://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.37/bin/apache-tomcat-9.0.37.exe";

  private File file;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download);

    // 开始下载和取消下载按钮
    findViewById(R.id.download_download_bt).setOnClickListener(this);
    findViewById(R.id.download_cancel_bt).setOnClickListener(this);

    // 暂停和继续下载按钮
    findViewById(R.id.download_pause_bt).setOnClickListener(this);
    findViewById(R.id.download_continue_bt).setOnClickListener(this);

    // 删除文件按钮
    Button button = findViewById(R.id.download_delete_bt);
    String fileName = this.down_url.substring(this.down_url.lastIndexOf("/"));
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    file = new File(path + fileName);
    if (!file.exists()) {
      // 如果文件不存在，设置按钮为禁用
      // button.setEnabled(false);
    }
    button.setOnClickListener(this);

    Intent intent = new Intent(this, DownloadService.class);
    // 创建服务
    startService(intent);
    // 绑定服务(建立服务和活动的关系)
    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    // 检查权限（权限不足则声明权限），权限还需要在manifest.xml中声明
    if (ContextCompat.checkSelfPermission(
            DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PermissionChecker.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          DownloadActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
  }

  /**
   * 响应用户授权结果
   *
   * @param requestCode 请求权限指定的id
   * @param permissions 权限
   * @param grantResults 用户授权结果
   */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
        // 申请写存储权限的id
      case 1:
        if (grantResults.length > 0 && grantResults[0] != PermissionChecker.PERMISSION_GRANTED) {
          // 权限申请失败，提示，退出App
          Toast.makeText(DownloadActivity.this, "用户未授权，无法使用App", Toast.LENGTH_SHORT).show();
          finish();
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.download_download_bt: // 点击下载按钮，获取到binder，调用下载文件的方法
      case R.id.download_continue_bt: // 继续下载(重新启动下载)
        serviceConnection.getDownloadBinder().startDownload(down_url);
        break;
      case R.id.download_cancel_bt:
        // 取消下载
        serviceConnection.getDownloadBinder().cancelDownload();
        break;
      case R.id.download_pause_bt:
        // 暂停下载
        serviceConnection.getDownloadBinder().pauseDownload();
        break;
      case R.id.download_delete_bt:
        // 删除已经下载的文件
        if (file.exists()) {
          file.delete();
          // findViewById(R.id.download_delete_bt).setEnabled(false);
          Toast.makeText(this, "已删除文件", Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        break;
    }
  }

  private static final String TAG = "DownloadActivity";

  // 活动连接到服务
  private DownloadServiceConnection serviceConnection = new DownloadServiceConnection();

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // 避免连接泄露
    unbindService(serviceConnection);
  }
}

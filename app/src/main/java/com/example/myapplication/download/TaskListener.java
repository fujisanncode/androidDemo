package com.example.myapplication.download;

public interface TaskListener {
  void onProgress(int progress);

  void onCancel();

  void onSuccess();

  void onFail();

  void onPause();
}

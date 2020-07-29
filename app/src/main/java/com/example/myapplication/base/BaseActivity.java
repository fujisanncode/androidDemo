package com.example.myapplication.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityHelper.remove(this);
    }

    private OfflineReceiveCast offlineReceiveCast;

    // 活动切换到栈顶，栈顶的活动注册到，广播接受器中；否则不需要注册（即不接受广播消息）
    @Override
    protected void onResume() {
        super.onResume();
        offlineReceiveCast = new OfflineReceiveCast();
        registerReceiver(offlineReceiveCast, new IntentFilter(offlineCast));
    }

    // 活动退出栈顶，广播从接受器中移除，只有栈顶的活动需要接受离线广播的消息
    @Override
    protected void onPause() {
        super.onPause();
        if(offlineReceiveCast != null) {
            unregisterReceiver(offlineReceiveCast);
        }
    }

    public static final String offlineCast = "com.example.myapplication.cast.offline";
    class OfflineReceiveCast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接受到消息，弹框离线
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.offline_assert);
            builder.setMessage(R.string.offline_assert_msg);
            // 不可取消弹框
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_bt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 关闭全部的活动
                    ActivityHelper.finishAll();
                }
            });
            builder.show();
        }
    }
}

package com.fujisann.ink;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fujisann.ink.base.BaseActivity;
import com.fujisann.ink.download.DownloadServiceConnection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String down_url = "https://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.37/bin/apache-tomcat-9.0.37.exe";

    @Override
    public void onClick(View view) {
        // switch (view.getId()) {
        //     case R.id.download_bt:
        //         serviceConnection.getDownloadBinder().startDownload(down_url);
        //         break;
        //     default:
        //         break;
        // }
    }

    private DownloadServiceConnection serviceConnection = new DownloadServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // findViewById(R.id.download_bt).setOnClickListener(this);
        // Intent intent = new Intent(this, DownloadService.class);
        // // 创建服务
        // startService(intent);
        // // 绑定服务(建立服务和活动的关系)
        // bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    private List<Fruit> fruitList = new ArrayList<>();

    private void initFruitList() {
        for (int i = 0; i < 10; i++) {
            fruitList.add(new Fruit("西瓜", R.drawable.strawberry));
            fruitList.add(new Fruit("香蕉", R.drawable.banana));
            fruitList.add(new Fruit("苹果", R.drawable.apple));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                Toast.makeText(MainActivity.this, R.string.menu_remove, Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                Toast.makeText(MainActivity.this, R.string.menu_add, Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }
}

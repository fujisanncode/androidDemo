package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.download.DownloadService;
import com.example.myapplication.download.DownloadServiceConnection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String down_url = "https://mirror.bit.edu.cn/apache/tomcat/tomcat-9/v9.0.37/bin/apache-tomcat-9.0.37.exe";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_bt:
                serviceConnection.getDownloadBinder().startDownload(down_url);
                break;
            default:
                break;
        }
    }

    private DownloadServiceConnection serviceConnection = new DownloadServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.download_bt).setOnClickListener(this);
        Intent intent = new Intent(this, DownloadService.class);
        // 创建服务
        startService(intent);
        // 绑定服务(建立服务和活动的关系)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        // 离线功能测试按钮
        // final Button offlineBt = findViewById(R.id.offline_bt);
        // offlineBt.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         // 通过本地广播广播消息（消息仅在app内广播），在baseActivity中接受消息
        //         sendBroadcast(new Intent(offlineCast));
        //     }
        // });

        // View bt = findViewById(R.id.bt1);
        // bt.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         // Toast.makeText(MainActivity.this,R.string.bt1,Toast.LENGTH_LONG).show();
        //         // finish();
        //         Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        //         startActivity(intent);
        //     }
        // });

        // 使用简单的列表样式
        // String[] fruit = {"苹果", "橘子", "橙子", "哈密瓜", "西瓜", "油桃", "蟠桃", "栗子", "苹果",
        //         "黑加仑", "李子", "葡萄", "相关", "桂圆", "荔枝", "山竹", "黄瓜", "白瓜", "香橙",
        //         "树莓", "草莓", "橘子", "砀山梨", "库尔勒香梨"};
        // ListView view = findViewById(R.id.list_view);
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
        //         R.layout.support_simple_spinner_dropdown_item, fruit);
        // view.setAdapter(adapter);

        // initFruitList();
        // // 加载自定义的list_item
        // FruitAdapter fruitAdapter = new FruitAdapter(MainActivity.this, R.layout.list_item, fruitList);
        // ListView view = findViewById(R.id.list_view);
        // // 设置子项目的样式和数据
        // view.setAdapter(fruitAdapter);

        // initFruitList();
        // FruitCycleAdapter adapter = new FruitCycleAdapter(fruitList);
        // RecyclerView id = findViewById(R.id.recycle);
        // id.setAdapter(adapter);
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

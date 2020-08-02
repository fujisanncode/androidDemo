package com.example.myapplication.material;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.FruitAdapter;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends AppCompatActivity {

  // 初始化toolbar的按钮布局
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_material);

    // 配置toolBar
    Toolbar toolbar = findViewById(R.id.material_toolbar);
    setSupportActionBar(toolbar);
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      // 显示左上角的按钮，并重设图标
      supportActionBar.setDisplayHomeAsUpEnabled(true);
      supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    // 保存布局对象
    this.drawerLayout = findViewById(R.id.draw_layout);

    // 设置左侧栏
    NavigationView navigationView = findViewById(R.id.nav_view);
    // 默认选中电话菜单
    navigationView.setCheckedItem(R.id.nav_tel);
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // 点击侧栏菜单，关闭侧栏
            drawerLayout.closeDrawers();
            return true;
          }
        });

    // 悬浮按钮
    FloatingActionButton floatingActionButton = findViewById(R.id.float_bt);
    floatingActionButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // snackBar
            Snackbar.make(v, "点击悬浮按钮", Snackbar.LENGTH_SHORT)
                .setAction(
                    "取消",
                    new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        Toast.makeText(MaterialActivity.this, "即将执行取消的逻辑", Toast.LENGTH_SHORT)
                            .show();
                      }
                    })
                .show();
          }
        });

    RecyclerView recyclerView = findViewById(R.id.material_fruit_list);
    // 设置布局管理器
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    // 设置数据
    initFruitList();
    final MaterialFruitAdapter fruitAdapter = new MaterialFruitAdapter(this.materialFruitList);
    recyclerView.setAdapter(fruitAdapter);

    final SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
    refreshLayout.setColorSchemeResources(R.color.colorPrimary);
    refreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            // 刷新的行为放到异步线程中，不阻塞ui线程
            new Thread(
                    new Runnable() {
                      @Override
                      public void run() {
                        try {
                          // 执行刷新逻辑
                          Thread.sleep(2000);
                        } catch (InterruptedException e) {
                          e.printStackTrace();
                        }
                        // 刷新逻辑执行结束，返回ui线程刷新ui数据
                        runOnUiThread(
                            new Runnable() {
                              @Override
                              public void run() {
                                initFruitList();
                                // 通知列表组件数据刷新
                                fruitAdapter.notifyDataSetChanged();
                                // 数据刷新后，停止刷新
                                refreshLayout.setRefreshing(false);
                              }
                            });
                      }
                    })
                .start();
          }
        });
  }

  private List<MaterialFruitAdapter.MaterialFruit> materialFruitList = new ArrayList<>();
  private FruitAdapter fruitAdapter;

  /** 初始化图片数据 */
  private void initFruitList() {
    for (int i = 0; i < 2; i++) {
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("苹果", R.drawable.apple));
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("香蕉", R.drawable.banana));
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("草莓", R.drawable.strawberry));
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("西瓜", R.drawable.watermelon));
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("桃子", R.drawable.peach));
      materialFruitList.add(new MaterialFruitAdapter.MaterialFruit("柠檬", R.drawable.lemon));
    }
  }

  // 菜单按钮的点击事件
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.toolbar_backup_bt:
        Toast.makeText(this, "备份按钮", Toast.LENGTH_SHORT).show();
        break;
      case R.id.toolbar_delete_bt:
        Toast.makeText(this, "删除按钮", Toast.LENGTH_SHORT).show();
        break;
      case R.id.toolbar_setting_bt:
        Toast.makeText(this, "设置按钮", Toast.LENGTH_SHORT).show();
        break;
      case android.R.id.home:
        // 点击左上角按钮，显示左侧栏，按钮id为内置的id定义的android.R.id.home
        this.drawerLayout.openDrawer(GravityCompat.START);
        break;
      default:
        break;
    }
    return true;
  }
}

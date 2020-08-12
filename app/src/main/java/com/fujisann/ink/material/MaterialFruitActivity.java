package com.fujisann.ink.material;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.fujisann.ink.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class MaterialFruitActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_material_fruit);

    // 设置toolbar
    Toolbar toolbar = findViewById(R.id.fruit_detail_toolbar);
    setSupportActionBar(toolbar);
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      // 显示左上角按钮
      supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    // 获取跳转过来的资源
    Intent intent = getIntent();
    String fruitName = intent.getStringExtra(FRUIT_NAME);
    int fruitId = intent.getIntExtra(FRUIT_ID, 1);
    // 标题
    CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.fruit_detail_collapse_bar);
    collapsingToolbarLayout.setTitle(fruitName);
    // 标题图片
    ImageView imageView = findViewById(R.id.fruit_detail_toolbar_img);
    Glide.with(this).load(fruitId).into(imageView);
    // 设置正文
    TextView textView = findViewById(R.id.material_fruit_detail_text);
    textView.setText(buildText(fruitName));

    FloatingActionButton floatingActionButton = findViewById(R.id.fruit_detail_float_bt);
    floatingActionButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // snapBar上可以设置按钮，用于提示后的交互
            Snackbar.make(v, "开发中", Snackbar.LENGTH_SHORT).show();
          }
        });
  }

  private String buildText(String name) {
    Random random = new Random();
    int loopCount = random.nextInt(1000);
    StringBuilder builder = new StringBuilder();
    for (int j = 0; j < loopCount; j++) {
      builder.append(name);
    }
    return builder.toString();
  }

  private static final String FRUIT_NAME = "fruitName";
  private static final String FRUIT_ID = "fruitId";

  /**
   * 跳转到此活动需要传递参数
   *
   * @param context 上下文
   * @param fruitName 水果名称
   * @param fruitId 水果图片id
   */
  public static void start(Context context, String fruitName, int fruitId) {
    Intent intent = new Intent(context, MaterialFruitActivity.class);
    intent.putExtra(FRUIT_NAME, fruitName);
    intent.putExtra(FRUIT_ID, fruitId);
    context.startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
        // 左上角返回按钮的点击事件
      case android.R.id.home:
        finish();
        break;
      default:
        break;
    }
    return true;
  }
}

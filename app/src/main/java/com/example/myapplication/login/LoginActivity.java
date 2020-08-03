package com.example.myapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.material.MaterialActivity;

public class LoginActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Button loginBt = findViewById(R.id.login_bt);
    loginBt.setOnClickListener(view -> {
            String loginAccount =
                ((TextView) findViewById(R.id.login_account)).getText().toString();
            String loginPass = ((TextView) findViewById(R.id.login_pass)).getText().toString();
            if (getResources().getString(R.string.expect_account).equals(loginAccount)
                && getResources().getString(R.string.expect_pass).equals(loginPass)) {
              startActivity(new Intent(LoginActivity.this, MaterialActivity.class));
              finish();
            } else {
              Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        }
        );
    // 设置默认登录用户名
    TextView account = findViewById(R.id.login_account);
    account.setText(getResources().getString(R.string.expect_account));
    TextView pass = findViewById(R.id.login_pass);
    pass.setText(getResources().getString(R.string.expect_pass));
  }
}

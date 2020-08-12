package com.fujisann.ink.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fujisann.ink.R;
import com.fujisann.ink.util.HttpUtil;
import com.fujisann.ink.weather.he.HeWeather;
import com.fujisann.ink.weather.he.HeWeatherDailyForecast;
import com.fujisann.ink.weather.he.Weather;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;
    private TextView weatherTitle;
    private TextView weatherUpdate;
    private TextView weatherDegree;
    private TextView weatherInfo;
    private LinearLayout forecastLayout;
    private TextView weatherSuggestionComfort;
    private TextView weatherSuggestionCarWash;
    private TextView weatherSuggestionSport;
    private TextView weatherAqiText;
    private TextView weatherPm25Text;
    private ImageView weatherBgImgView;
    private Button homeButton;
    public DrawerLayout areaDrawer;
    public SwipeRefreshLayout weatherSwipeRefresh;

    public static final String WEATHER_ID = "weatherId";

    public static void start(Context context, String weatherId){
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(WEATHER_ID, weatherId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String weatherId = intent.getStringExtra(WEATHER_ID);

        // 融入状态栏
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        this.weatherLayout = findViewById(R.id.weather_layout);
        this.weatherTitle = findViewById(R.id.weather_title);
        this.weatherUpdate = findViewById(R.id.weather_update);
        this.weatherDegree = findViewById(R.id.weather_now_degree);
        this.weatherInfo = findViewById(R.id.weather_now_info);
        this.forecastLayout = findViewById(R.id.weather_forecast_layout);
        this.weatherAqiText = findViewById(R.id.weather_aqi_text);
        this.weatherPm25Text = findViewById(R.id.weather_pm25_text);
        this.weatherSuggestionComfort = findViewById(R.id.weather_suggest_comfort);
        this.weatherSuggestionCarWash = findViewById(R.id.weather_suggest_car_wash);
        this.weatherSuggestionSport = findViewById(R.id.weather_suggest_sport);
        this.weatherBgImgView = findViewById(R.id.weather_bg_img);
        this.homeButton = findViewById(R.id.area_home_bt);
        this.areaDrawer = findViewById(R.id.weather_drawer);
        this.weatherSwipeRefresh = findViewById(R.id.weather_swipe_refresh);
        this.weatherSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        // 请求背景图片, 如果缓存中存在数据则直接加载图片
        String weatherBgImgUrl = PreferenceManager.getDefaultSharedPreferences(this).getString(WEATHER_BG_IMG_URL, null);
        if(TextUtils.isEmpty(weatherBgImgUrl)) {
            requestBgImg();
        } else {
            loadBgImg(weatherBgImgUrl);
        }

        // 请求天气数据, 如果缓存中存在数据则从缓存中获取
        String weatherTxt = PreferenceManager.getDefaultSharedPreferences(this).getString(WEATHER_TXT, null);
        if(TextUtils.isEmpty(weatherTxt)) {
            // 请求天气数据前先隐藏组件，避免空显示
            this.weatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherData(weatherId);
        } else {
            Weather weather = new Gson().fromJson(weatherTxt, Weather.class);
            showWeatherData(weather);
        }

        this.weatherSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 请求天气数据
                requestWeatherData(weatherId);
            }
        });

        this.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDrawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public static final String WEATHER_TXT = "weatherTxt";
    public static final String WEATHER_BG_IMG_URL = "weatherBgImgUrl";

    public void requestWeatherData(String weatherId){
        String url = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.send(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                weatherSwipeRefresh.setRefreshing(false);
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String weatherTxt = response.body().string();
                final Weather weather = new Gson().fromJson(weatherTxt, Weather.class);

                // 缓存从服务器请求的天气数据
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString(WEATHER_TXT, weatherTxt);
                edit.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 显示天气数据
                        showWeatherData(weather);
                        weatherLayout.setVisibility(View.VISIBLE);

                        // 刷新结束
                        weatherSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeatherData(Weather weather){
        HeWeather heWeather = weather.getHeWeather().get(0);
        weatherTitle.setText(heWeather.getBasic().getCity());
        weatherUpdate.setText(heWeather.getBasic().getUpdate().getLoc().split(" ")[1]);
        weatherDegree.setText(heWeather.getNow().getTmp() + "℃");
        weatherInfo.setText(heWeather.getNow().getCond().getTxt());
        weatherAqiText.setText(heWeather.getAqi().getCity().getAqi());
        weatherPm25Text.setText(heWeather.getAqi().getCity().getPm25());
        weatherSuggestionComfort.setText(heWeather.getSuggestion().getComf().getTxt());
        weatherSuggestionCarWash.setText(heWeather.getSuggestion().getCw().getTxt());
        weatherSuggestionSport.setText(heWeather.getSuggestion().getSport().getTxt());
        // 清空view，重新加载
        forecastLayout.removeAllViews();
        List<HeWeatherDailyForecast> forecastList = heWeather.getForecastList();
        for (HeWeatherDailyForecast temp : forecastList) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView weatherDate = view.findViewById(R.id.weather_date_text);
            TextView weatherInfo = view.findViewById(R.id.weather_info_text);
            TextView weatherMax = view.findViewById(R.id.weather_max_text);
            TextView weatherMin = view.findViewById(R.id.weather_min_text);
            weatherDate.setText(temp.getDate());
            weatherInfo.setText(temp.getCond().getTxtD());
            weatherMax.setText(temp.getTmp().getMax());
            weatherMin.setText(temp.getTmp().getMin());
            forecastLayout.addView(view);
        }
    }

    private void requestBgImg(){
        String weatherBgImg = "http://guolin.tech/api/bing_pic";
        HttpUtil.send(weatherBgImg, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String bgImgUrl = response.body().string();
                // 保存图片地址
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("weatherBgImgUrl", bgImgUrl);
                edit.apply();

                // 图片加载到main线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBgImg(bgImgUrl);
                    }
                });
            }
        });
    }

    private void loadBgImg(String bgImgUrl){
        Glide.with(WeatherActivity.this).load(bgImgUrl).into(weatherBgImgView);
    }
}
package com.fujisann.ink.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fujisann.ink.MainActivity;
import com.fujisann.ink.R;
import com.fujisann.ink.util.HttpUtil;
import com.fujisann.ink.weather.region.City;
import com.fujisann.ink.weather.region.County;
import com.fujisann.ink.weather.region.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegionFragment extends Fragment {
    private TextView titleTextView;
    private Button backButton;
    private ListView dataListView;

    // 列表数据适配器
    ArrayAdapter<String> arrayAdapter;
    // 列表数据
    private List<String> dataList = new ArrayList<>();
    // 列表数据对应的对象集合
    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();

    // 当前选择省/市/县
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    // 应该查询数据对应的数据等级
    private int dataLevel;
    // 数据层级定义
    public static final int province = 1;
    public static final int city = 2;
    public static final int county = 3;

    public static final String SELECT_CITY = "selectCity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 如果从缓存中读取到选择的城市，则直接显示天气
        if(getActivity() instanceof MainActivity) {
            String cacheCity = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SELECT_CITY, null);
            if(!TextUtils.isEmpty(cacheCity)) {
                WeatherActivity.start(getActivity(), cacheCity);
                getActivity().finish();
            }
        }

        View view = inflater.inflate(R.layout.region_fragment, container);
        this.titleTextView = view.findViewById(R.id.fragment_title);
        this.backButton = view.findViewById(R.id.fragment_back_bt);
        this.dataListView = view.findViewById(R.id.fragment_data_list);
        this.arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                dataList);
        dataListView.setAdapter(arrayAdapter);
        return view;
    }

    // 根据按钮的点击，动态添加数据到list中
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 列表点击：省份列表、城市列表
        this.dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击到下层，显示返回按钮
                backButton.setVisibility(View.VISIBLE);
                switch (dataLevel) {
                    case province:
                        // 设置当前选定的省份
                        selectedProvince = provinceList.get(position);
                        titleTextView.setText(selectedProvince.getName());
                        // 并将下次查询的数据等级设置为城市级别
                        dataLevel = city;
                        break;
                    case city:
                        selectedCity = cityList.get(position);
                        titleTextView.setText(selectedCity.getName());
                        dataLevel = county;
                        break;
                    case county:
                        // 在城市层点击，需要调整到天气页面查询数据
                        String weatherId = countyList.get(position).getWeatherId();

                        // 保存选择的城市
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        edit.putString(SELECT_CITY, weatherId);
                        edit.apply();

                        if(getActivity() instanceof MainActivity) {
                            // main选择城市，跳转到weather
                            WeatherActivity.start(getActivity(), weatherId);
                            // 关闭main
                            getActivity().finish();
                        } else if(getActivity() instanceof  WeatherActivity) {
                            // weather的抽屉选择城市，关闭抽屉，重新请求数据
                            WeatherActivity activity = (WeatherActivity) getActivity();
                            activity.areaDrawer.closeDrawers();
                            activity.weatherSwipeRefresh.setRefreshing(true);
                            // 不关闭weather
                            activity.requestWeatherData(weatherId);
                        }
                    default:
                        break;
                }
                resetDataList();
            }
        });
        // 返回按钮点击
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dataLevel) {
                    case county:
                        titleTextView.setText(selectedProvince.getName());
                        dataLevel = city;
                        break;
                    case city:
                        titleTextView.setText("中国");
                        dataLevel = province;
                        backButton.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                // 重置列表数据
                resetDataList();
            }
        });

        // 初始化为省份列表
        this.titleTextView.setText("中国");
        this.dataLevel = province;
        backButton.setVisibility(View.GONE);
        resetDataList();
    }

    public void resetDataList() {
        // 从数据库查询省数据是否存在，如果存在数据保存到集合中
        // 如果数据不存在，起线程查询(防止查询慢导致主线程卡死)，查到数据保存数据库，保存到list中
        // 切换到主线程，给页面塞数据

        StringBuilder url = new StringBuilder("http://guolin.tech/api/china");
        switch (dataLevel) {
            case province:
                this.provinceList = DataSupport.findAll(Province.class);
                if (this.provinceList.size() != 0) {
                    updateProvinceData();
                    this.arrayAdapter.notifyDataSetChanged();
                    return;
                }
                break;
            case city:
                this.cityList = DataSupport.where("provinceId = ?", String.valueOf(this.selectedProvince.getId()))
                        .find(City.class);
                if (this.cityList.size() != 0) {
                    updateCityData();
                    this.arrayAdapter.notifyDataSetChanged();
                    return;
                }
                url.append("/").append(this.selectedProvince.getCode());
                break;
            case county:
                this.countyList = DataSupport.where("cityId = ?", String.valueOf(this.selectedCity.getId()))
                        .find(County.class);
                if (countyList.size() != 0) {
                    updateCountyData();
                    this.arrayAdapter.notifyDataSetChanged();
                    return;
                }
                url.append("/").append(this.selectedProvince.getCode()).append("/").append(this.selectedCity.getCode());
                break;
            default:
                break;
        }

        // 调用接口查询数据，并在页面显示
        doQuery(this.dataLevel, url.toString());
    }

    private static final String TAG = "RegionFragment";

    public void doQuery(int currentLevel, String url) {
        HttpUtil.send(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();

                switch (currentLevel) {
                    case province:
                        // 数据保存数据库
                        Type provinceType = new TypeToken<List<Province>>() {
                        }.getType();
                        provinceList = gson.fromJson(json, provinceType);
                        Province.saveAll(provinceList);
                        // 修改列表数据
                        updateProvinceData();
                        break;
                    case city:
                        Type cityType = new TypeToken<List<City>>() {
                        }.getType();
                        cityList = gson.fromJson(json, cityType);
                        for (City cityTemp : cityList) {
                            cityTemp.setProvinceId(selectedProvince.getId());
                        }
                        City.saveAll(cityList);
                        // 修改列表数据
                        updateCityData();
                        break;
                    case county:
                        Type countyType = new TypeToken<List<County>>() {
                        }.getType();
                        countyList = gson.fromJson(json, countyType);
                        for (County countyTemp : countyList) {
                            countyTemp.setCityId(selectedCity.getId());
                        }
                        County.saveAll(countyList);
                        // 修改列表数据
                        updateCountyData();
                        break;
                    default:
                        break;
                }

                // 数据改变通知到ui线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void updateProvinceData() {
        dataList.clear();
        for (Province province : provinceList) {
            dataList.add(province.getName());
        }
    }

    private void updateCityData() {
        dataList.clear();
        for (City city : cityList) {
            dataList.add(city.getName());
        }
    }

    private void updateCountyData() {
        dataList.clear();
        for (County county : countyList) {
            dataList.add(county.getName());
        }
    }

    private <T> List<T> buildData(Class<T> clazz, Response response) throws IOException {
        return new Gson().fromJson(response.body().string(), new TypeToken<List<T>>() {
        }.getType());
    }

}

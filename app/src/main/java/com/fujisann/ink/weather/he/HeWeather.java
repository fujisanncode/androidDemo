package com.fujisann.ink.weather.he;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HeWeather {
    private HeWeatherBasic basic;
    private HeWeatherUpdate update;
    private String status;
    private HeWeatherNow now;
    @SerializedName("daily_forecast")
    private List<HeWeatherDailyForecast> forecastList;
    private HeWeatherAqi aqi;
    private HeWeatherSuggestion suggestion;
    private String msg;

    public HeWeatherBasic getBasic() {
        return basic;
    }

    public void setBasic(HeWeatherBasic basic) {
        this.basic = basic;
    }

    public HeWeatherUpdate getUpdate() {
        return update;
    }

    public void setUpdate(HeWeatherUpdate update) {
        this.update = update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HeWeatherNow getNow() {
        return now;
    }

    public void setNow(HeWeatherNow now) {
        this.now = now;
    }

    public List<HeWeatherDailyForecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<HeWeatherDailyForecast> forecastList) {
        this.forecastList = forecastList;
    }

    public HeWeatherAqi getAqi() {
        return aqi;
    }

    public void setAqi(HeWeatherAqi aqi) {
        this.aqi = aqi;
    }

    public HeWeatherSuggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(HeWeatherSuggestion suggestion) {
        this.suggestion = suggestion;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package com.fujisann.ink.weather.region;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private transient int id;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String code;

    private transient int cityId;

    @SerializedName("weather_id")
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}

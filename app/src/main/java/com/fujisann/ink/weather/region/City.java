package com.fujisann.ink.weather.region;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {
    private transient int id;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String code;

    private transient int provinceId;

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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}

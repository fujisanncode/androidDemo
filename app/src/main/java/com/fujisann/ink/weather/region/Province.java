package com.fujisann.ink.weather.region;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {
    private transient int id;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String code;

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

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

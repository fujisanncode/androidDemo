package com.fujisann.ink;

public class Fruit {
    // 水果名称
    private String name;
    // 水果图片id
    private int sourceId;

    public Fruit(String name, int sourceId) {
        this.name = name;
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}

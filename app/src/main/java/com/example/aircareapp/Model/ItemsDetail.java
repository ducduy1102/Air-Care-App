package com.example.aircareapp.Model;

public class ItemsDetail {
    private String name;
    private String dataAsset;
    private int icon;

    public ItemsDetail(String name, String dataAsset, int icon) {
        this.name = name;
        this.dataAsset = dataAsset;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataAsset() {
        return dataAsset;
    }

    public void setDataAsset(String dataAsset) {
        this.dataAsset = dataAsset;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

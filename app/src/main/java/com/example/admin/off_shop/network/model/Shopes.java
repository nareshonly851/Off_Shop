
package com.example.admin.off_shop.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shopes {

    @SerializedName("offers")
    @Expose
    private List<Shop> shops = null;

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

}

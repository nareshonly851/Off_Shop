
package com.example.admin.off_shop.network.model.offers;

import java.util.List;

import com.example.admin.off_shop.network.model.Shop;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offers {

    @SerializedName("shops")
    @Expose
    private List<Shop> shops = null;

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

}

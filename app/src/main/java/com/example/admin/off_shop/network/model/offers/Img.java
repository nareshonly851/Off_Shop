
package com.example.admin.off_shop.network.model.offers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Img {

    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("offer_id")
    @Expose
    private Integer offerId;
    @SerializedName("image_path")
    @Expose
    private String imagePath;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}


package com.example.admin.off_shop.network.model.offers;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {

    @SerializedName("offer_id")
    @Expose
    private Integer offerId;
    @SerializedName("offer_unique_id")
    @Expose
    private String offerUniqueId;
    @SerializedName("shop_Id")
    @Expose
    private Integer shopId;
    @SerializedName("offer_Title")
    @Expose
    private String offerTitle;
    @SerializedName("offer_Desc")
    @Expose
    private String offerDesc;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("offer_condition")
    @Expose
    private String offerCondition;
    @SerializedName("start_Date")
    @Expose
    private String startDate;
    @SerializedName("end_Date")
    @Expose
    private String endDate;
    @SerializedName("store_date")
    @Expose
    private String storeDate;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("img")
    @Expose
    private List<Img> img = null;

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public String getOfferUniqueId() {
        return offerUniqueId;
    }

    public void setOfferUniqueId(String offerUniqueId) {
        this.offerUniqueId = offerUniqueId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getOfferCondition() {
        return offerCondition;
    }

    public void setOfferCondition(String offerCondition) {
        this.offerCondition = offerCondition;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
        this.storeDate = storeDate;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<Img> getImg() {
        return img;
    }

    public void setImg(List<Img> img) {
        this.img = img;
    }

}

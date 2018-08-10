
package com.vivy.testtask;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Serialized and map the data from JSON
 * Creates Getters ans Setters
 */

public class Doctor {


    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("highlighted")
    @Expose
    private Boolean highlighted;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("lat")
    @Expose
    private Double lat; //latitude

    @SerializedName("lng")
    @Expose
    private Double lng; // longitude

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("openingHours")
    @Expose
    private List<String> openingHours = null;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("photoId")
    @Expose
    private String photoId;

    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("reviewCount")
    @Expose
    private Integer reviewCount;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("specialityIds")
    @Expose
    private List<Integer> specialityIds = null;

    @SerializedName("website")
    @Expose
    private String website;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<String> openingHours) {
        this.openingHours = openingHours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Integer> getSpecialityIds() {
        return specialityIds;
    }

    public void setSpecialityIds(List<Integer> specialityIds) {
        this.specialityIds = specialityIds;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}

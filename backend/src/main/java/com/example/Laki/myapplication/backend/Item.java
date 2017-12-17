package com.example.Laki.myapplication.backend;

/**
 * Created by Laki on 29/10/2017.
 */

public class Item {

    private String id;
    private String title;
    private String giverName;
    private String givenLocation;
    private String itemDescription;
    private String itemCategory;
    private String itemAgeGroup;
    private String giverAvatarUrl;
    private String itemUrl;
    private String timePosted;
    private String itemStatus;
    private float longitude;
    private float latitude;
    private String giverUUID;
    private String pickLocation;
    private String pickTime;

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGiverUUID() {
        return giverUUID;
    }

    public void setGiverUUID(String giverUUID) {
        this.giverUUID = giverUUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGiverName() {
        return giverName;
    }

    public void setGiverName(String giverName) {
        this.giverName = giverName;
    }

    public String getGivenLocation() {
        return givenLocation;
    }

    public void setGivenLocation(String givenLocation) {
        this.givenLocation = givenLocation;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getPickLocation() {
        return pickLocation;
    }

    public void setPickLocation(String pickLocation) {
        this.pickLocation = pickLocation;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemAgeGroup() {
        return itemAgeGroup;
    }

    public void setItemAgeGroup(String itemAgeGroup) {
        this.itemAgeGroup = itemAgeGroup;
    }

    public String getGiverAvatarUrl() {
        return giverAvatarUrl;
    }

    public void setGiverAvatarUrl(String giverAvatarUrl) {
        this.giverAvatarUrl = giverAvatarUrl;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}

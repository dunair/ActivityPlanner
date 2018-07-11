package org.exp.activityplanner;

import java.util.List;

/**
 * Created by a-7775 on 7/10/18.
 */
public class ActivityDBModel {
    private long id;
    private String title;
    private String fromPrice;
    private String duration;
    private List<String> categories;
    private String latLng;
    private String scoreOutOf5;
    private String imageUrl;
    private boolean reviewScoreThresholdMet;
    private String cityName;
    private String preferences;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(String fromPrice) {
        this.fromPrice = fromPrice;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getScoreOutOf5() {
        return scoreOutOf5;
    }

    public void setScoreOutOf5(String scoreOutOf5) {
        this.scoreOutOf5 = scoreOutOf5;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isReviewScoreThresholdMet() {
        return reviewScoreThresholdMet;
    }

    public void setReviewScoreThresholdMet(boolean reviewScoreThresholdMet) {
        this.reviewScoreThresholdMet = reviewScoreThresholdMet;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}

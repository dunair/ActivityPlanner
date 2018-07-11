package org.exp.activityplanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by a-7775 on 7/8/18.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    private long id;
    private String title;
    private String fromPrice;
    private String duration;
    private List<String> categories;
    private String latLng;
    private String scoreOutOf5;
    private String imageUrl;
    private boolean reviewScoreThresholdMet;

    public boolean isFreeCancellation() {
        return freeCancellation;
    }

    public void setFreeCancellation(boolean freeCancellation) {
        this.freeCancellation = freeCancellation;
    }

    private boolean freeCancellation = false;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getScoreOutOf5() {
        return scoreOutOf5;
    }

    public void setScoreOutOf5(String scoreOutOf5) {
        this.scoreOutOf5 = scoreOutOf5;
    }

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

    public boolean isReviewScoreThresholdMet() {
        return reviewScoreThresholdMet;
    }

    public void setReviewScoreThresholdMet(boolean reviewScoreThresholdMet) {
        this.reviewScoreThresholdMet = reviewScoreThresholdMet;
    }
}

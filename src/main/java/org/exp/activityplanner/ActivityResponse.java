package org.exp.activityplanner;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by a-7775 on 7/9/18.
 */
public class ActivityResponse {
    Long activityId;
    String activityName;
    String fromPrice;
    String duration;
    String imageUrl;
    String score;
    WeatherResponse weather = new WeatherResponse();
    List<String> inclusions = new ArrayList<>();
    boolean freeCancellation;
    String staticMapUrl = new String("");



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public WeatherResponse getWeather() {
        return weather;
    }

    public void setWeather(WeatherResponse weather) {
        this.weather = weather;
    }

    public List<String> getInclusions() {
        return inclusions;
    }

    public void setInclusions(List<String> inclusions) {
        this.inclusions = inclusions;
    }

    public boolean isFreeCancellation() {
        return freeCancellation;
    }

    public void setFreeCancellation(boolean freeCancellation) {
        this.freeCancellation = freeCancellation;
    }

    public String getStaticMapUrl() {
        return staticMapUrl;
    }

    public void setStaticMapUrl(String staticMapUrl) {
        this.staticMapUrl = staticMapUrl;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}

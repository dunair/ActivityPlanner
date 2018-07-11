package org.exp.activityplanner;

public class Request {

    String cityName;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    String action;

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    String preferences;

    public Request() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
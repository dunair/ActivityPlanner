package org.exp.activityplanner;

/**
 * Created by a-7775 on 7/9/18.
 */
public class WeatherResponse {
    int temp;
    String description = new String("");

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

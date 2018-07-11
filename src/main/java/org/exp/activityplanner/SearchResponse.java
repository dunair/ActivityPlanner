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
public class SearchResponse {
    private List<Activity> activities = new ArrayList<>();

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}

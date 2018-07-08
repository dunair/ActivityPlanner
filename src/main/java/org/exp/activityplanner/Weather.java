package org.exp.activityplanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Created by a-7775 on 7/8/18.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    long id;
    String main;
    String description;

    public Weather() {

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

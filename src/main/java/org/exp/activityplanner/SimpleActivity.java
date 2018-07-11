package org.exp.activityplanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by a-7775 on 7/10/18.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleActivity {
    String id;
    List<String> inclusions = new ArrayList<>();
    boolean freeCancellation;
    String staticMapUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

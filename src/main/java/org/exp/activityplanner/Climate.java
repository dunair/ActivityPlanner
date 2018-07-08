package org.exp.activityplanner;

import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by a-7775 on 7/8/18.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Climate {
    String message;
    String cod;
    String count;
    List<ClimateDetails> list = new ArrayList<>();

    public Climate() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ClimateDetails> getList() {
        return list;
    }

    public void setList(List<ClimateDetails> list) {
        this.list = list;
    }
}

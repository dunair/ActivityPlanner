package org.exp.activityplanner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * Created by a-7775 on 7/8/18.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String geo_type;
    private BigDecimal lat;
    private BigDecimal lng;
    private String street;
    private String city;

    public String getGeo_type() {
        return geo_type;
    }

    public void setGeo_type(String geo_type) {
        this.geo_type = geo_type;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

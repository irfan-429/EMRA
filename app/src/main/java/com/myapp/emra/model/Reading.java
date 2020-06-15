package com.myapp.emra.model;


import java.util.HashMap;
import java.util.Map;


public class Reading {

    private String image;
    private String reading;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

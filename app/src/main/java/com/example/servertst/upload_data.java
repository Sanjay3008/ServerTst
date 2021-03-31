package com.example.servertst;

import com.google.firebase.database.Exclude;

public class upload_data {

    private String sample_name;
    private String location;
    private String Acoustic_value;
    private String Mineral;
    private String imageurl;
    private String key;
    private String moisture;
    private String voltage;
    private String ifrared;
    private String Sodium;
    private String Magesium;

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getIfrared() {
        return ifrared;
    }

    public void setIfrared(String ifrared) {
        this.ifrared = ifrared;
    }

    public String getSodium() {
        return Sodium;
    }

    public void setSodium(String sodium) {
        Sodium = sodium;
    }

    public String getMagesium() {
        return Magesium;
    }

    public void setMagesium(String magesium) {
        Magesium = magesium;
    }

    public String getCalcium() {
        return Calcium;
    }

    public void setCalcium(String calcium) {
        Calcium = calcium;
    }

    private String Calcium;

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAcoustic_value() {
        return Acoustic_value;
    }

    public void setAcoustic_value(String acoustic_value) {
        Acoustic_value = acoustic_value;
    }

    public String getMineral() {
        return Mineral;
    }

    public void setMineral(String mineral) {
        Mineral = mineral;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    @Exclude
    public void setKey(String key)
    {
        this.key = key;
    }
    @Exclude
    public String getKey()
    {
        return key;
    }


    public upload_data() {

    }

    public upload_data(String sample_name, String location, String imageurl, String moisture, String voltage, String ifrared, String sodium, String magesium, String calcium) {
        this.sample_name = sample_name;
        this.location = location;
        this.imageurl = imageurl;
        this.moisture = moisture;
        this.voltage = voltage;
        this.ifrared = ifrared;
        Sodium = sodium;
        Magesium = magesium;
        Calcium = calcium;
    }


}

package com.cowin.vaccine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Center {

    public int center_id;
    public String name;
    public String address;
    public String state_name;
    public String district_name;
    public String block_name;
    public int pincode;

    @JsonIgnore
    @JsonProperty("lat")
    public int latitude;

    @JsonIgnore
    @JsonProperty("long")
    public int longitude;

    public String from;
    public String to;
    public String fee_type;
    public List<Session> sessions;
    public List<VaccineFee> vaccine_fees;

    public Center() {}

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<VaccineFee> getVaccine_fees() {
        return vaccine_fees;
    }

    public void setVaccine_fees(List<VaccineFee> vaccine_fees) {
        this.vaccine_fees = vaccine_fees;
    }

    public String getCenterDetails() {
        return "Name : " + this.name + "\n" + "Address : " + this.address + "\n" + "Pincode : " + this.pincode + "\n";
    }
}

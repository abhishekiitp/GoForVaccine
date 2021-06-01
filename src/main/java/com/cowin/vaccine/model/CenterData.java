package com.cowin.vaccine.model;

import java.util.HashMap;
import java.util.Map;

public class CenterData {

    Map<String, Centers> centersMap = new HashMap<>();

    public CenterData() {}

    public Map<String, Centers> getCentersMap() {
        return centersMap;
    }

    public void setCentersMap(Map<String, Centers> centersMap) {
        this.centersMap = centersMap;
    }
}

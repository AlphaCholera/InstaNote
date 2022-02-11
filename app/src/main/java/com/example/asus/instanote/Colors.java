package com.example.asus.instanote;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    private static Map<Integer, Integer> colorHashMap = new HashMap<>();

    public Colors() {
    }

    public Colors(Map<Integer, Integer> colorHashMap) {
        this.colorHashMap = colorHashMap;
    }

    public Map<Integer, Integer> getColorHashMap() {
        return colorHashMap;
    }

    public void setColorHashMap(Map<Integer, Integer> colorHashMap) {
        this.colorHashMap = colorHashMap;
    }
}

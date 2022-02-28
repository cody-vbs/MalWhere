package com.codyvbs.malwhere;

public class Adapter {
    private static String detected_URL;

    public static String getDetected_URL() {
        return detected_URL;
    }

    public static void setDetected_URL(String detected_URL) {
        Adapter.detected_URL = detected_URL;
    }
}

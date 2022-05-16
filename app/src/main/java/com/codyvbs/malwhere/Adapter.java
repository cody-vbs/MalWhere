package com.codyvbs.malwhere;

public class Adapter {
    private static String detected_URL;
    private static String finalLongURL;
    public static String MyGuestPresf = "GUEST_USER";
    public static String MainUserPresf = "MAIN_USER";
    public static final String urlShortenerDomain [] = {"bit.ly","tinyurl","is.gd","bit.do","cutt.ly","tinyurl.com","s.id",
            "t.co","gg.gg","rebrand.ly","abre.ai","short.io","linkly","clickmeter","pixelme","blink","SmartURL","soo.gd","tiny.cc","clkim.com","t2mio.com",
    "tiny.ie","www.shorturl.at","yourls.org","www.musicjet.com","adf.ly","goog.gl","ow.ly","buff.ly","mcaf.ee","su.pr","polr","budurl.co","polrproject.org",
    "MOOURL.COM","surbl.org","guru99.com","dynomapper.com"};

    public static String SUBMIT_REPORT_URL_ONLINE = "https://malwhereapp.com/submit_report.php";
    public static String SUBMIT_REPORT_URL_LOCAL = "http://192.168.1.4/MalWhere/submit_report.php";


    public static String getDetected_URL() {
        return detected_URL;
    }

    public static void setDetected_URL(String detected_URL) {
        Adapter.detected_URL = detected_URL;
    }

    public static String getFinalLongURL() {
        return finalLongURL;
    }

    public static void setFinalLongURL(String finalLongURL) {
        Adapter.finalLongURL = finalLongURL;
    }
}

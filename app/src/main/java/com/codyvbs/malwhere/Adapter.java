package com.codyvbs.malwhere;

public class Adapter {
    private static String detected_URL;
    public static final String urlShortenerDomain [] = {"bit.ly","tinyurl","is.gd","bit.do","cutt.ly","tinyurl.com","s.id",
            "t.co","gg.gg","rebrand.ly","abre.ai","short.io","linkly","clickmeter","pixelme","blink","SmartURL","soo.gd","tiny.cc","clkim.com","t2mio.com",
    "tiny.ie","www.shorturl.at","yourls.org","www.musicjet.com","adf.ly","goog.gl","ow.ly","buff.ly","mcaf.ee","su.pr","polr","budurl.co","polrproject.org",
    "MOOURL.COM","surbl.org","guru99.com","dynomapper.com"};


    public static String getDetected_URL() {
        return detected_URL;
    }

    public static void setDetected_URL(String detected_URL) {
        Adapter.detected_URL = detected_URL;
    }
}

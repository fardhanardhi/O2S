package com.cumaprokerajasih.o2sApp;

import android.annotation.SuppressLint;

public class Config {

    // API URL configuration
    public static String ADMIN_PANEL_URL = "https://cumaprokerajasih.000webhostapp.com/";

    // change this access similar with accesskey in admin panel for security reason
    public static String AccessKey = "12345";

    // database path configuration
    @SuppressLint("SdCardPath")
    public static String DBPath = "/data/data/com.cumaprokerajasih.o2sApp/databases/";

}

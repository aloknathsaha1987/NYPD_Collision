package com.aloknath.nypd_collision.HttpManager;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ALOKNATH on 4/11/2015.
 */
public class HttpManager {

    public static String getData(String urlPassed) {

        BufferedReader reader;
        HttpURLConnection con;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            URL url = new URL(urlPassed);
            con = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.i("WEBService", sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

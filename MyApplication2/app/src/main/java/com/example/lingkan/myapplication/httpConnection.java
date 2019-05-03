package com.example.lingkan.myapplication;
/**
 * Created by lingkan on 22/11/2017.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class httpConnection {
    private static final String TAG = httpConnection.class.getSimpleName();
    public static String Json = "";

    public String getJSONFromUrl(String url) {

        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();
            int status = connection.getResponseCode();

            //catching HTTP 200 and 201 errors
            switch (status) {
                case 200:
                case 201:
                    //live connection
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //string which store Json data to be returned to REST
                    StringBuilder sb = new StringBuilder();
                    String line;

                    //loop through and retrun data
                    while ((line = buffer.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    buffer.close();

                    // remember, you are storing the json as a stringy
                    try {
                        Json = sb.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing data " + e.toString());
                    }
                    // return JSON String containing data to Tweet activity (or whatever your activity is called!)
                    return Json;

            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return null;
    }
}

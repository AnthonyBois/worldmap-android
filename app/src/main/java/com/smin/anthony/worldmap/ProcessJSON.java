package com.smin.anthony.worldmap;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Anthony on 22/05/2018.
 */

public class ProcessJSON extends AsyncTask<String, Void, String> {
    private static String iso;
    protected String doInBackground(String... strings){
        /*String stream = null;
        String urlString = strings[0];

        HTTPDataHandler hh = new HTTPDataHandler();
        stream = hh.GetHTTPData(urlString);*/
       /* try {
            urlWelcome = new URL("http://www.google.fr/intl/en_com/images/srpr/logo1w.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        welcome = (ImageView) findViewById(R.id.imageView);
        downloadImage();*/
        return null;
    }

    public void onPostExecute(String stream){
        if(stream !=null){

        }
    }
}
package com.smin.anthony.worldmap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import static android.location.LocationManager.NETWORK_PROVIDER;


public class MapActivity extends Activity implements LocationListener, Response.Listener<String>, Response.ErrorListener{
    private LocationManager lManager;
    private Location location;
    String urlLatLongISO;
    String iso = null;
    ImageView welcome;
    URL urlWelcome = null;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                0);

        setContentView(R.layout.activity_map);



        setContentView(R.layout.activity_map);
        //On récupère le service de localisation
        lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Initialisation de l'écran
        reinitialisationEcran();

        obtenirPosition();

    }

    //Réinitialisation de l'écran
    private void reinitialisationEcran(){
        ((TextView)findViewById(R.id.adresse)).setText("...");
    }



    public void obtenirPosition() {
        lManager.requestLocationUpdates(NETWORK_PROVIDER, 60000, 0, this);
    }

    public void requestISO2() {
        obtenirPosition();
        urlLatLongISO = "https://api.opencagedata.com/geocode/v1/json?q="+location.getLatitude()+"%2C%20"+location.getLongitude()+"&key=3cb5d4185cb14bbc93797e291549a2c7&language=fr&pretty=1";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, urlLatLongISO, this, this);
        queue.add(request);
    }

    public void requestDetails(View v){
        urlLatLongISO = "https://restcountries.eu/rest/v2/alpha?codes=" + iso;
        Log.d("sdf",""+iso);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, urlLatLongISO, this, this);
        queue.add(request);

    }

    public void onLocationChanged(Location location) {
        setProgressBarIndeterminateVisibility(false);
        this.location = location;
        requestISO2();
        lManager.removeUpdates(this);
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(MapActivity.this, String.format("Le GPS a été désactivé, veuillez activer la position", provider), Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onResponse(String response) {
        try{
            JSONObject jObj;
            JSONArray results;
            jObj = new JSONObject(response);
            results = (JSONArray) jObj.get("results");
            JSONObject components;
            JSONObject id;
            id = results.getJSONObject(0);
            components = id.getJSONObject("components");
            afficheLocation(results);
        }catch(JSONException e){
            e.printStackTrace();
        }

        try{
            JSONArray jObj;
            jObj = new JSONArray(response);    // json is the JSON string
            JSONObject id;
            id = jObj.getJSONObject(0);
            String pays = id.getString("name");
            afficheDetails(jObj);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void afficheDetails(JSONArray jObj){
        try {
            JSONObject id = jObj.getJSONObject(0);
            String pays = id.getString("name");
            String capital = id.getString("capital");
            String continent = id.getString("region");
            String population = id.getString("population");
            JSONArray monnaie = id.getJSONArray("currencies");
            JSONObject idMonnaie = monnaie.getJSONObject(0);
            String nomMonnaie = idMonnaie.getString("name");
            String symbolMonnaie = idMonnaie.getString("symbol");
            String drapeau = id.getString("flag");

            webView = (WebView) findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(30);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.loadUrl(drapeau);
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed() ;
                }
            });

            ((TextView)findViewById(R.id.name)).setText(pays);
            ((TextView)findViewById(R.id.capital)).setText(capital);
            ((TextView)findViewById(R.id.continent)).setText(continent);
            ((TextView)findViewById(R.id.population)).setText(population+" habitants");
            ((TextView)findViewById(R.id.monnaie)).setText(nomMonnaie+" ("+symbolMonnaie+")");
            Log.d("coucoucouc",""+pays);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void afficheLocation(JSONArray results){
        try {
            JSONObject components;
            JSONObject id;
            id = results.getJSONObject(0);
            components = id.getJSONObject("components");
            iso = components.getString("country_code");
            String city = components.getString("city");
            String country = components.getString("country");
            ((TextView) findViewById(R.id.adresse)).setText(city + " - " + country);
            ((Button) findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {}
}



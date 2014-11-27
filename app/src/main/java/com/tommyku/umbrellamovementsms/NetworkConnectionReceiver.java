package com.tommyku.umbrellamovementsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class NetworkConnectionReceiver extends BroadcastReceiver {

    private static final String spreadSheet = "http://spreadsheets.google.com/feeds/list/16vLHc92nBdN8QjgEq5NKlgq4Rde_5E0QmPlGk7GwHOQ/od6/public/values?alt=json";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private final String SETTING = "umbrellaMovementSMS_Setting";

    public NetworkConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            settings = context.getSharedPreferences(SETTING, 0);
            editor = settings.edit();

            // new getLatestNumbers().execute(spreadSheet);

            // grab a new list of SMS numbers from the google calendar
            // Toast.makeText(context, "You are now online.", Toast.LENGTH_SHORT).show();
        }
    }

    private class getLatestNumbers extends AsyncTask<String, Integer, JSONObject> {
        private InputStream is = null;
        private JSONObject jObj = null;

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(urls[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                jObj = new JSONObject(sb.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            // return JSON String
            return jObj;
        }

        protected void onPostExecute(JSONObject json) {
            // update the sharedPref list
            JSONArray jsArr = new JSONArray();
            JSONArray jsTmpArr;
            try {
                jsTmpArr = json.getJSONObject("feed").getJSONArray("entry");
                // for each entry
                for (int i = 0; i < jsTmpArr.length(); i++) {
                    JSONObject jsTmpObj = ((JSONObject) jsTmpArr.get(i));
                    JSONObject tmpToAdd = new JSONObject();
                    tmpToAdd.put("area", jsTmpObj.getString("gsx$area"));
                    tmpToAdd.put("number", jsTmpObj.getString("gsx$number"));
                    Log.v("INFO", jsTmpObj.getString("gsx$area"));
                    Log.v("INFO", jsTmpObj.getString("gsx$number"));
                    jsTmpArr.put(jsTmpArr.length(), tmpToAdd);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

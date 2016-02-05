package com.example.davidberg.androidkurs;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by davidberg on 05/02/16.
 */
public class Vasttrafik extends AsyncTask<Void,Integer,Boolean> {
    private static final String URL_AUTHENTICATION_TOKEN = "https://api.vasttrafik.se/token";

    @Override
    protected Boolean doInBackground(Void... v){
        return getAuth();
    }

    @Override
    protected void onPostExecute(Boolean bool) {
    }

    private Boolean getAuth(){
        URL url;
        try {
            url = new URL(URL_AUTHENTICATION_TOKEN);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("User-Agent","SoapUI/5.0.0");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Host", "auth.vasttrafik.se");
            conn.setRequestProperty("Connection", "Keep-Alive");

            // These are from https://developer.vasttrafik.se/portal/#/overview
            conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString("iTzjSsDAPEd0Iu2qyVEm2ikm2XMa:esAejfT3iHX9IkcAzbbmXN3Js70a".getBytes(), Base64.DEFAULT));

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write("grant_type=client_credentials&scope=device_" + Build.MODEL);
            writer.flush();
            writer.close();
            out.close();

            int responseCode =conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("VASTTRAFIK", "Response HTTP_OK: "+responseCode);
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    Log.d("VASTTRAFIK", "Line: "+line);
                }
            }
            else {
                Log.e("VASTTRAFIK", "Response NOT_OK: "+responseCode);
            }

            return true;
        }catch(Exception e){
            Log.e("VASTTRAFIK", "Can't connect!");
            e.printStackTrace();
            return false;
        }
    }

}

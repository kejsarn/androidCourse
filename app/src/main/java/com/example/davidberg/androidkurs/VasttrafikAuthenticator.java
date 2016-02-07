package com.example.davidberg.androidkurs;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by davidberg on 05/02/16.
 */
public class VasttrafikAuthenticator extends AsyncTask<Void,Integer,VasttrafikAuthInfo> {
    private static final String URL_AUTHENTICATION_TOKEN = "https://api.vasttrafik.se/token";

    private VasttrafikAuthInfo retObj = new VasttrafikAuthInfo();


    @Override
    protected VasttrafikAuthInfo doInBackground(Void... v){
        return getAuth();
    }

    @Override
    protected void onPostExecute(VasttrafikAuthInfo vAuth) {

    }

    private VasttrafikAuthInfo getAuth(){
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

            GZIPInputStream gzin = new GZIPInputStream(conn.getInputStream());
            JsonReader reader = new JsonReader(new InputStreamReader(gzin, "UTF-8"));
            reader.setLenient(true);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("VASTTRAFIK", "Response HTTP_OK: "+responseCode);

                try {
                    reader.beginObject();
                    while(reader.hasNext()){
                        Log.d("VASTTRAFIK", "Reader hasNext() ");
                        String nextName = reader.nextName();
                        Log.d("VASTTRAFIK", "Reader nextName:"+nextName);
                        if(nextName.equals("access_token")){
                            retObj.setAccessToken(reader.nextString());
                        }else if(nextName.equals("expires_in")){
                             retObj.setExpirationTime(reader.nextInt());
                        }else{
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    reader.close();
                    Log.d("VASTTRAFIK", "Access Token: "+retObj.getAccessToken());
                    Log.d("VASTTRAFIK", "Exporation Time: "+retObj.getExpirationTime());
                } catch (Exception e){
                    Log.e("VASTTRAFIK", "JSON exception: ");
                    e.printStackTrace();
                }
            } else {
                Log.e("VASTTRAFIK", "Response NOT_OK: " + responseCode);
            }
            return retObj;
        }catch(Exception e){
            Log.e("VASTTRAFIK", "Can't connect!");
            e.printStackTrace();
            return retObj;
        }
    }
}

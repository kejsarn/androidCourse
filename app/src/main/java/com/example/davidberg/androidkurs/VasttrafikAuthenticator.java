package com.example.davidberg.androidkurs;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by davidberg on 05/02/16.
 *
 * Used to interface Vasttrafiks OAuth.
 *
 */
public class VasttrafikAuthenticator extends AsyncTask<Void,Integer,VasttrafikAuthenticatorInfo> {
    // These are from https://developer.vasttrafik.se/portal/#/overview
    private static final String URL_AUTHENTICATION_TOKEN = "https://api.vasttrafik.se/token";
    private static final String CLIENT_IDENTIFIER = "iTzjSsDAPEd0Iu2qyVEm2ikm2XMa";
    private static final String CLIENT_SECRET = "esAejfT3iHX9IkcAzbbmXN3Js70a";

    private VasttrafikAuthenticatorInfo retObj = new VasttrafikAuthenticatorInfo();
    VasttrafikAuthenticatorCaller vc;

    VasttrafikAuthenticator(VasttrafikAuthenticatorCaller vc) {
        this.vc = vc;
    }

    @Override
    protected VasttrafikAuthenticatorInfo doInBackground(Void... v){
        return getAuth();
    }

    @Override
    protected void onPostExecute(VasttrafikAuthenticatorInfo vAuth) {
        vc.onAuthReceived(vAuth);
    }

    private VasttrafikAuthenticatorInfo getAuth(){
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

            conn.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((CLIENT_IDENTIFIER + ":" + CLIENT_SECRET).getBytes(), Base64.DEFAULT));

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
                Log.d("VASTTRAFIK", "Auth Response HTTP_OK: "+responseCode);

                try {
                    reader.beginObject();
                    while(reader.hasNext()){
                        Log.d("VASTTRAFIK", "Reader hasNext() ");
                        String nextName = reader.nextName();
                        Log.d("VASTTRAFIK", "Reader nextName:"+nextName);

                        // We're only interested in two of the fields; access_token and expires_in
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
                Log.e("VASTTRAFIK", "Auth Response NOT_OK: " + responseCode);
            }
            return retObj;
        }catch(Exception e){
            Log.e("VASTTRAFIK", "Can't connect!");
            e.printStackTrace();
            return retObj;
        }
    }
}

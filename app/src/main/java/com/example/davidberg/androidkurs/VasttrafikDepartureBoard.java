package com.example.davidberg.androidkurs;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by davidberg on 07/02/16.
 */
public class VasttrafikDepartureBoard extends AsyncTask<String,Integer,List<VasttrafikJourney>> {

    private static final String URL_DEPARTURE_BOARD = "https://api.vasttrafik.se/bin/rest.exe/v2/departureBoard";

    @Override
    protected List<VasttrafikJourney> doInBackground(String... stopAndToken){
        try {
            if (stopAndToken.length > 2) {
                throw new Exception("Don't support multiple stopIds in one call");
            }
            return getJourneys(stopAndToken[0], stopAndToken[1]);
        }catch (Exception e){
            Log.e("VASTTRAFIK", "Departure Boards called with multiple stopIds!");
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<VasttrafikJourney> journeys){

    }

    private List<VasttrafikJourney> getJourneys(String stopId, String token){
        List<VasttrafikJourney> retList = new ArrayList<VasttrafikJourney>();
        URL url;
        try{
            // Korsvägen = 9021014003980000
            url = new URL(URL_DEPARTURE_BOARD+"?id="+stopId+"&format=json");
            //url = new URL("https://api.vasttrafik.se/bin/rest.exe/v2/departureBoard?date=2016-02-07&direction=9021014007171000&id=9021014003980000&time=14:55");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("User-Agent", "SoapUI/5.0.0");
            conn.setRequestProperty("Host", "api.vasttrafik.se");
            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Authorization", "Bearer "+token);

            int responseCode =conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("VASTTRAFIK", "DepartureBoard Response OK" + responseCode);
                GZIPInputStream gzin = new GZIPInputStream(conn.getInputStream());
                String jsonString = null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(gzin));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null ){
                    sb.append(line + "\n");
                }
                jsonString = sb.toString();
                JSONObject jObject = new JSONObject(jsonString);
                JSONObject DepartureBoard = jObject.getJSONObject("DepartureBoard");
                JSONArray departures = DepartureBoard.getJSONArray("Departure");

                for (int i=0; i < departures.length(); i++)
                {
                    try {
                        JSONObject departure = departures.getJSONObject(i);
                        VasttrafikJourney v = new VasttrafikJourney();
                        v.setName(departure.getString("name"));
                        v.setSname(departure.getString("sname"));
                        v.setDirection(departure.getString("direction"));
                        v.setTime(departure.getString("rtTime"));
                        v.setJourneyId(departure.getString("journeyid"));
                        retList.add(v);
                        Log.d("VASTTRAFIK","Departure; Name: "+v.getName()+", Direction: "+v.getDirection()+", Time: "+v.getTime());
                    } catch (JSONException e) {
                        Log.e("VASTTRAFIK", "Departure parsing error");
                        e.printStackTrace();
                    }
                }

            }else{
                Log.e("VASTTRAFIK", "DepartureBoard Response NOT_OK: " + responseCode);
            }
        }catch (Exception e){
            Log.e("VASTTRAFIK", "DepartureError!");
            e.printStackTrace();
        }
        return retList;
    }
}

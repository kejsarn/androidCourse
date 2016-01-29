package com.example.davidberg.androidkurs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private PHHueSDK phHueSDK;
    Settings set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        AnalogClock a = (AnalogClock) findViewById(R.id.analogClock);
        a.setOnLongClickListener(this);

        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        BroadcastReceiver bcr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean flight = intent.getBooleanExtra("state", false);
                TextView tv = (TextView) findViewById(R.id.textView3);
                tv.setText("Flight: " + flight);
            }
        };
        this.registerReceiver(bcr, filter);

        phHueSDK = PHHueSDK.getInstance();
        Log.d("CREATION", "onCreate being executed!");

        set = new Settings();
    }

    private void setUpHue(){
        phHueSDK.setAppName("HueTram");
        phHueSDK.setDeviceName(Build.MODEL);
        phHueSDK.getNotificationManager().registerSDKListener(HueListener);
        Log.d("CREATION", "setUpHue() executed !");
    }

    private PHSDKListener HueListener = new PHSDKListener() {


        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {

        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);

        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {

            phHueSDK.startPushlinkAuthentication(phAccessPoint);
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
            Log.d("CREATION", "Access point found!");
        }

        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {

        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {

        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.davidsmeny, menu);
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        // Do something in response to button click
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Trying Hue!");
        Context c = getApplicationContext();
        Toast t = Toast.makeText(c, "Long clicked!", Toast.LENGTH_SHORT);
        t.show();
        setUpHue();

        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Context c = getApplicationContext();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.Ny) {
            Toast t = Toast.makeText(c, "Ny", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        if (id == R.id.Spara) {
            set.Spara(this);
            Toast t = Toast.makeText(c, "Spara", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        if (id == R.id.Ladda) {
            set.Ladda(this);
            Toast t = Toast.makeText(c, "Ladda", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SwitchClicked(View v) {
        Switch sw = (Switch) v;
        TextView tv = (TextView) findViewById(R.id.textView);
        AnalogClock c = (AnalogClock) findViewById(R.id.analogClock);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();


        if (sw.isChecked()) {
            sw.setText("På!");
            tv.setText("Klockan var: " + df.format(d) + " senaste switch");
            c.setRotationX((float) 45.0);
        } else {
            sw.setText("Av!");
            c.setRotationX((float) 0.0);
        }
    }

    public void NyAktivitetKlickad(View v) {
        TextView tv = (TextView) findViewById(R.id.textView);

        int reqCode = 12;
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("text", tv.getText());
        startActivityForResult(i, reqCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle b = data.getExtras();
        String inst = b.getString("Inst");
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("Inst: " + inst);
    }

    public void ClockLongClicked(View v) {

    }

    public void sparaKnappKlickad(View v) {
        set.Spara(this);
    }

    public void laddaKnappKlickad(View v) {
        set.Ladda(this);
    }

    public void onConnectClick(View v) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new NikonConnectionTask().execute("http://www.vecka.nu/");
        } else {
            // Set text
        }
    }

    private class NikonConnectionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                Context c = getApplicationContext();
                Toast t = Toast.makeText(c, "Connection error!", Toast.LENGTH_SHORT);
                t.show();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv = (TextView) findViewById(R.id.textView4);
            tv.setText(result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
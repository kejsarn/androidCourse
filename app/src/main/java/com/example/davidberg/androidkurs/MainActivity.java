package com.example.davidberg.androidkurs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, VasttrafikAuthenticatorCaller {

    private PHHueSDK phHueSDK;
    private VasttrafikAuthenticatorInfo vAuth;

    //private JourneyAdapter jAdapter;
    Settings set;

    private RecyclerView mRecyclerView;
    private JourneyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //ListView lv = (ListView) findViewById(R.id.listView);
        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //jAdapter = new JourneyAdapter(lv.getContext(), journeys);
        mAdapter = new JourneyAdapter();
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        phHueSDK = PHHueSDK.getInstance();
        Log.d("CREATION", "onCreate being executed!");
        vAuth = new VasttrafikAuthenticatorInfo();
        set = new Settings();
    }

    private void setUpHue(){
        phHueSDK.setAppName("HueTram");
        phHueSDK.setDeviceName(Build.MODEL);
        phHueSDK.getNotificationManager().registerSDKListener(HueListener);
        Log.d("CREATION", "setUpHue() executed !");
    }

    public PHSDKListener HueListener = new DavidsPHSDKListener(this);

    private class DavidsPHSDKListener implements PHSDKListener {

        public DavidsPHSDKListener(AppCompatActivity a){
            this.a = a;
        }


        public void setA(AppCompatActivity a) {
            this.a = a;
        }

        private AppCompatActivity a;

        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
            Log.d("CREATION", "Cache Updated!");
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            String userName = "";
            String ip = "";
            Log.d("CREATION", "Bridge connected!");
            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            set.SaveUsernameAndIp(a, phHueSDK.getAccessPointsFound().get(0).getUsername(), phHueSDK.getAccessPointsFound().get(0).getIpAddress());
            invalidateOptionsMenu();
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
            Log.d("CREATION", "Auth required!");
            phHueSDK.startPushlinkAuthentication(phAccessPoint);
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
            Log.d("CREATION", "Access point found!");
            if(list != null && list.size()==1){
                phHueSDK.getAccessPointsFound().clear();
                phHueSDK.getAccessPointsFound().addAll(list);

                phHueSDK.connect(list.get(0));
            }
        }

        @Override
        public void onError(int i, String s) {
            Log.d("CREATION", "ErrrorNr: "+i+" text: "+s);
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            Log.d("CREATION", "Connection resumed!");
        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            Log.d("CREATION", "Connection!");
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
            Log.d("CREATION", "Parsing error!");

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.davidsmeny, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        Log.d("CREATION", "onPrepareOptionsMenu!");
        if(phHueSDK != null && phHueSDK.getSelectedBridge() != null) {
            PHBridge b = phHueSDK.getSelectedBridge();
            List<PHLight> lights = b.getResourceCache().getAllLights();
            ListIterator<PHLight> it = lights.listIterator();
            while (it.hasNext()) {
                menu.add(it.next().getName());
            }
        }
        return true;
    }


    @Override
    public boolean onLongClick(View v) {
        // Do something in response to button click
        Context c = getApplicationContext();
        Toast t = Toast.makeText(c, "Connecting bridge!", Toast.LENGTH_SHORT);
        t.show();
        setUpHue();


        String[] returned = new String[2];
        returned = set.LoadUsernameAndIp(this);
        String userName = returned[0];
        String ip = returned[1];
        if(userName != "NULL" && ip!="NULL"){
            Log.d("CREATION", "Saved UserName/Ip found!");
            PHAccessPoint lastAccessPoint = new PHAccessPoint();
            lastAccessPoint.setIpAddress(ip);
            lastAccessPoint.setUsername(userName);
            if(!phHueSDK.isAccessPointConnected(lastAccessPoint)){
                Log.d("CREATION", "Trying to connect!");
                phHueSDK.connect(lastAccessPoint);
            }
        }else {
            Log.d("CREATION", "No saved UserName/Ip found!");
            PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
            sm.search(true, true);
        }
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
            Toast t = Toast.makeText(c, "Spara", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        if (id == R.id.Ladda) {
            Toast t = Toast.makeText(c, "Ladda", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sparaKnappKlickad(View v) {

    }

    public void laddaKnappKlickad(View v) {

    }

    public void hueButtonClicked(View v){

        PHBridge bridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache cache = bridge.getResourceCache();

        List<PHLight> myLights = cache.getAllLights();
        Log.d("CREATION", myLights.toString());

        float xy[] = PHUtilities.calculateXYFromRGB(0, 255, 0, myLights.get(0).getModelNumber());
        PHLightState lightState = new PHLightState();
        lightState.setX(xy[0]);
        lightState.setY(xy[1]);

        bridge.updateLightState((PHLight) (myLights.get(0)), lightState, new PHLightListener() {
            @Override
            public void onReceivingLightDetails(PHLight phLight) {

            }

            @Override
            public void onReceivingLights(List<PHBridgeResource> list) {
                Log.d("CREATION", "onReceivingLights()");
            }

            @Override
            public void onSearchComplete() {
                Log.d("CREATION", "onSearchComplete()");
            }

            @Override
            public void onSuccess() {
                Log.d("CREATION", "New hue sent successfully!");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("CREATION", "onError()");
            }

            @Override
            public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {
                Log.d("CREATION", "onStateUpdate()");
            }
        });
    }

    public boolean onConnectClick(View v) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new VasttrafikAuthenticator(this) {
                @Override
                protected void onPostExecute(VasttrafikAuthenticatorInfo vAuth) {
                    Log.d("CREATION", "vAuth.getAccessToken(): "+vAuth.getAccessToken());
                    Log.d("CREATION", "vAuth.getExpirationTime(): " + vAuth.getExpirationTime());
                    TextView txt = (TextView) findViewById(R.id.textViewToken);
                    txt.setText(vAuth.getAccessToken());
                    TextView txtExp = (TextView) findViewById(R.id.textViewExpires);
                    txtExp.setText(vAuth.getExpirationTime().toString());
                    super.onPostExecute(vAuth);
                }
            }.execute();
        } else {
                        // Set text
        }
        return true;
    }

    public boolean getDepClicked(View v){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if(vAuth.isSet()) {
                Log.d("VASTTRAFIK", "getDepClicked() vAuth set: "+vAuth.getAccessToken());
                new VasttrafikDepartureBoard() {
                    @Override
                    protected void onPostExecute(List<VasttrafikJourney> journeys) {
                        updateJourneyList(journeys);
                    }

                    @Override
                    protected void onProgressUpdate(VasttrafikJourney... journey) {
/*                        Log.d("VASTTRAFIK", "onProgressUpdate()");
                        if(journey.length>1){
                            Log.e("VASTTRAFIK", "onProgressUpdate input argument length error");
                            return;
                        }
                        mAdapter.addItem(journey[0]);*/
                    }

                }.execute("9021014003980000", vAuth.getAccessToken());
            }else{
                Log.d("VASTTRAFIK", "getDepClicked() vAuth not set: "+vAuth.getAccessToken());
            }
        }
    // norra ullevi: 9021014007171000
        //korsv: 9021014003980000

        return true;
    }

    public void onAuthReceived(VasttrafikAuthenticatorInfo vAuth){
        Log.d("VASTTRAFIK", "onAuthReceived() token: "+vAuth.getAccessToken());
        Log.d("VASTTRAFIK", "onAuthReceived() expire: " + vAuth.getExpirationTime());
        this.vAuth = vAuth;
    }

    private void updateJourneyList(List<VasttrafikJourney> journeys){
        mAdapter.addAll(journeys);
        mAdapter.removePastDepartures();
    }



    //public VasttrafikAuthenticatorInfo getAuth(){
    //    return vAuth;
    //}

    //public void setAuth(VasttrafikAuthenticatorInfo auth){
    //    vAuth = auth;
    //}
}
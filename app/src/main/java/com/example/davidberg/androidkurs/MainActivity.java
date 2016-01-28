package com.example.davidberg.androidkurs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

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
                TextView tv = (TextView)findViewById(R.id.textView3);
                tv.setText("Flight: "+flight);
            }
        };
        this.registerReceiver(bcr, filter);

        set = new Settings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.davidsmeny, menu);
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        // Do something in response to button click
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Long Clicked!");
        Context c = getApplicationContext();
        Toast t = Toast.makeText(c, "Long clicked!", Toast.LENGTH_SHORT);
        t.show();
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

    public void SwitchClicked(View v){
        Switch sw = (Switch)v;
        TextView tv = (TextView)findViewById(R.id.textView);
        AnalogClock c = (AnalogClock)findViewById(R.id.analogClock);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();


        if(sw.isChecked()){
            sw.setText("På!");
            tv.setText("Klockan var: " + df.format(d) + " senaste switch");
            c.setRotationX((float) 45.0);
        }else{
            sw.setText("Av!");
            c.setRotationX((float) 0.0);
        }
    }

    public void NyAktivitetKlickad(View v){
        TextView tv = (TextView)findViewById(R.id.textView);

        int reqCode = 12;
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("text", tv.getText());
        startActivityForResult(i, reqCode);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        Bundle b = data.getExtras();
        String inst = b.getString("Inst");
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Inst: " + inst);
    }
    public void ClockLongClicked(View v){

    }

    public void sparaKnappKlickad(View v){
        set.Spara(this);
    }

    public void laddaKnappKlickad(View v){
        set.Ladda(this);
    }
}

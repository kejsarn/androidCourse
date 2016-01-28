package com.example.davidberg.androidkurs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        String val = extras.getString("text");
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText(val);
    }

    public void stangKlickad(View v){
        EditText tv = (EditText)findViewById(R.id.editText);
        Intent data = new Intent();
        data.putExtra("Inst", tv.getText().toString());
        //data.putExtra("Inst", "Yo!");
        setResult(RESULT_OK, data);
        finish();
    }

    public void dialogKlickad(View v){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("En alert dialog!");
        b.setCancelable(true);
        b.setPositiveButton("Yepp!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button b = (Button)findViewById(R.id.button3);
                b.setText("Yepp!");
            }
        });
        b.setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button b = (Button)findViewById(R.id.button3);
                b.setText("Nope!");
            }
        });

        AlertDialog d = b.create();
        d.show();

    }
}

package com.kostovtd.martini_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kostovtd.martini.Martini;
import com.kostovtd.martini.SmsListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Martini.with(this).start();
    }

}

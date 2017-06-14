package com.kostovtd.martini_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kostovtd.martini.Martini;
import com.kostovtd.martini.PhoneCallListener;
import com.kostovtd.martini.SmsListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Martini.with(this)
                .setType(Martini.SMS_AND_PHONE_CALL_TYPE)
                .setPhoneCallListener(new PhoneCallListener() {
                    @Override
                    public void onPhoneCallReceived(String phoneNumber) {
                        Log.d(TAG, "onPhoneCallReceived: hit");
                    }
                })
                .start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Martini.with(this).stop();
    }
}

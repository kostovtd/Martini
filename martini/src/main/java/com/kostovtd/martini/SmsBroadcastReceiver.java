package com.kostovtd.martini;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostovtd on 30.05.17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();
    private static final String PDUS_KEY = "pdus";

    private SmsBroadcastListener smsBroadcastListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: hit");

        if (intent != null) {
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(PDUS_KEY);

                if (pdusObj != null) {
                    List<SmsMessage> smsMessageList = new ArrayList<>();

                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentSms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        smsMessageList.add(currentSms);
                    }

                    if (smsBroadcastListener != null) {
                        smsBroadcastListener.onMessagesReceived(smsMessageList);
                    }
                }
            }
        }
    }


    public void setSmsBroadcastListener(SmsBroadcastListener smsBroadcastListener) {
        this.smsBroadcastListener = smsBroadcastListener;
    }
}

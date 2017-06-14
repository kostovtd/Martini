package com.kostovtd.martini;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by kostovtd on 14.06.17.
 */

public class PhoneCallBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneCallBroadcastReceiver.class.getSimpleName();
    private static final String INCOMING_NUMBER_EXTRA_KEY = "incoming_number";

    private PhoneCallBroadcastListener phoneCallBroadcastListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: hit");

        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            int callState = telephonyManager.getCallState();

            switch (callState) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String phoneNumber = getPhoneNumberFromIntent(intent);

                    if(phoneCallBroadcastListener != null) {
                        phoneCallBroadcastListener.onCallReceived(phoneNumber);
                    }

                    break;
            }
        }
    }


    public void setPhoneCallBroadcastListener(PhoneCallBroadcastListener phoneCallBroadcastListener) {
        this.phoneCallBroadcastListener = phoneCallBroadcastListener;
    }


    /**
     * Get the phone number from the {@link Intent}
     * associated with the incoming call.
     * @param intent
     * @return A phone number if available, or EMPTY {@link String} otherwise
     */
    private String getPhoneNumberFromIntent(Intent intent) {
        if(intent != null) {
            return intent.getStringExtra(INCOMING_NUMBER_EXTRA_KEY);
        }

        return "";
    }
}

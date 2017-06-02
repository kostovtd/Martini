package com.kostovtd.martini;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostovtd on 25.05.17.
 */

public class Martini {

    private static final String TAG = Martini.class.getSimpleName();
    private static final String BROADCAST = "android.provider.Telephony.SMS_RECEIVED";


    private static Martini singleton = null;
    private final Context context;
    private SmsListener smsListener;
    private List<String> gatewayList;
    private SmsBroadcastReceiver smsBroadcastReceiver;



    private Martini(Context context) {
        this.context = context;
        this.gatewayList = new ArrayList<>();
    }


    /**
     * The default way to obtain a {@link Martini} instance.
     */
    public static Martini with(Context context) {
        Log.d(TAG, "with: hit");

        if (context == null) {
            throw new IllegalArgumentException("context can NOT be NULL");
        }

        if (singleton == null) {
            singleton = new Martini(context);
        }

        return singleton;
    }


    public Martini setSmsListener(SmsListener smsListener) {
        if (smsListener == null) {
            throw new IllegalArgumentException("smsListener can not be NULL");
        }

        this.smsListener = smsListener;

        return singleton;
    }


    /**
     * Register a gateway for receiving messages from.
     *
     * @param gateway
     */
    public Martini addGateway(String gateway) {
        Log.d(TAG, "addGateway: hit");

        if (Is.empty(gateway)) {
            Log.i(TAG, "empty gateway");
            return singleton;
        }

        gatewayList.add(gateway);
        Log.i(TAG, "included gateway: " + gateway);

        return singleton;
    }


    /**
     * Register gateways for receiving messages from.
     *
     * @param gateways
     */
    public Martini addGateways(String[] gateways) {
        Log.d(TAG, "addGateways: hit");

        if (Is.empty(gateways)) {
            Log.i(TAG, "empty gateways");
            return singleton;
        }


        for (String gateway : gateways) {
            Log.i(TAG, "included gateway: " + gateway);
            gatewayList.add(gateway);
        }

        return singleton;
    }


    /**
     * Register gateways for receiving messages from.
     *
     * @param gatewayList
     */
    public Martini addGateways(ArrayList<String> gatewayList) {
        Log.d(TAG, "addGateways: hit");

        if (Is.empty(gatewayList)) {
            Log.i(TAG, "empty gateways");
            return singleton;
        }

        this.gatewayList = gatewayList;

        return singleton;
    }


    public List<String> getGatewayList() {
        return gatewayList;
    }


    /**
     * Start a {@link android.content.BroadcastReceiver} for
     * listening for each incoming SMS.
     */
    public void start() {
        Log.d(TAG, "start: hit");

        if(smsBroadcastReceiver == null) {
            smsBroadcastReceiver = new SmsBroadcastReceiver();
        }


        smsBroadcastReceiver.setBroadcastListener(new BroadcastListener() {
            @Override
            public void onMessagesReceived(List<SmsMessage> smsMessageList) {
                for(SmsMessage smsMessage : smsMessageList) {
                    String smsNumber = smsMessage.getDisplayOriginatingAddress();
                    if(!Is.empty(smsNumber)) {
                        Log.d(TAG, "onMessagesReceived: " + smsNumber);

                        boolean isKnownGateway = isKnownGateway(smsNumber);

                        if(!isKnownGateway) {
                            Log.i(TAG, "onMessagesReceived: not interesting number");
                            continue;
                        }

                        if(smsListener == null) {
                            Log.i(TAG, "onMessagesReceived: smsListener is NULL");
                            continue;
                        }

                        smsListener.onSmsReceived(smsMessage);
                    }
                }
            }
        });


        IntentFilter intentFilter = new IntentFilter(BROADCAST);

        context.registerReceiver(smsBroadcastReceiver, intentFilter);
    }


    /**
     * Stop a previously started {@link android.content.BroadcastReceiver}
     */
    public void stop() {
        Log.d(TAG, "stop: hit");

        if(smsBroadcastReceiver != null) {
            context.unregisterReceiver(smsBroadcastReceiver);
        }
    }



    public void clearGatewayList() {
        Log.d(TAG, "clearGatewayList: hit");

        if(gatewayList != null) {
            gatewayList.clear();
        }
    }


    /**
     * Determine if the given number is part of the gateways
     * for which we care about.
     * @param smsNumber
     * @return TRUE if the number is part of important gateways of us.
     * FALSE otherwise.
     */
    private boolean isKnownGateway(String smsNumber) {
        for(String gateway : gatewayList) {
            if(gateway.trim().toLowerCase().equals(smsNumber.trim().toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
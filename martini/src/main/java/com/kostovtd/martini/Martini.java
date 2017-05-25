package com.kostovtd.martini;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostovtd on 25.05.17.
 */

public class Martini {

    private static final String TAG = Martini.class.getSimpleName();


    private static Martini singleton = null;
    private final Context context;
    private SmsListener smsListener;
    private List<String> gatewayList;


    private Martini(Context context) {
        this.context = context;
    }


    /**
     * The default way to obtain a {@link Martini} instance.
     */
    public static Martini with(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("context can NOT be NULL");
        }

        if(singleton == null) {
            singleton = new Martini(context);
        }

        return singleton;
    }


    public Martini setSmsListener(SmsListener smsListener) {
        if(smsListener == null) {
            throw new IllegalArgumentException("smsListener can not be NULL");
        }

        this.smsListener = smsListener;

        return singleton;
    }


    /**
     * Register a gateway for receiving messages from.
     * @param gateway
     */
    public Martini addGateway(String gateway) {
        if(Is.empty(gateway)) {
            Log.i(TAG, "empty gateway");
            return singleton;
        }

        if(gatewayList == null) {
            gatewayList = new ArrayList<>();
        }

        gatewayList.add(gateway);
        Log.i(TAG, "included gateway: " + gateway);

        return singleton;
    }


    /**
     * Register gateways for receiving messages from.
     * @param gateways
     */
    public Martini addGateways(String[] gateways) {
        if(Is.empty(gateways)) {
            Log.i(TAG, "empty gateways");
            return singleton;
        }

        if(gatewayList == null) {
            gatewayList = new ArrayList<>();
        }

        for(String gateway: gateways) {
            Log.i(TAG, "included gateway: " + gateway);
            gatewayList.add(gateway);
        }

        return singleton;
    }


    /**
     * Register gateways for receiving messages from.
     * @param gatewayList
     */
    public Martini addGateways(ArrayList<String> gatewayList) {
        if(Is.empty(gatewayList)) {
            Log.i(TAG, "empty gateways");
            return singleton;
        }

        this.gatewayList = gatewayList;

        return singleton;
    }
}

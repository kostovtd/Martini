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

    public static final String RECEIVED_SMS_BROADCAST = "android.provider.Telephony.SMS_RECEIVED";
    public static final String PHONE_STATE_BROADCAST = "android.intent.action.PHONE_STATE";
    public static final int SMS_AND_PHONE_CALL_TYPE = 0;    // listen for both SMSs and phone calls
    public static final int SMS_TYPE = 1;   // listen only for SMSs
    public static final int PHONE_CALL_TYPE = 2;    // listen only for phone calls


    private static Martini singleton = null;
    private final Context context;
    private int type;

    private SmsListener smsListener;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private List<String> gatewayList;

    private PhoneCallListener phoneCallListener;
    private PhoneCallBroadcastReceiver phoneCallBroadcastReceiver;


    private Martini(Context context) {
        this.context = context;
        this.gatewayList = new ArrayList<>();
        this.type = SMS_AND_PHONE_CALL_TYPE;    //by default both SMS and phone call authentication are enabled
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


    public Martini setPhoneCallListener(PhoneCallListener phoneCallListener) {
        if (phoneCallListener == null) {
            throw new IllegalArgumentException("phoneCallListener can not be NULL");
        }

        this.phoneCallListener = phoneCallListener;

        return singleton;
    }


    /**
     * Set the type of authentication we should care about.
     * If the given type does not mach any of the available types,
     * then {@value SMS_AND_PHONE_CALL_TYPE} is used by default.
     *
     * @param type
     * @return
     */
    public Martini setType(int type) {
        switch (type) {
            case SMS_AND_PHONE_CALL_TYPE:
                this.type = SMS_AND_PHONE_CALL_TYPE;
                break;
            case SMS_TYPE:
                this.type = SMS_TYPE;
                break;
            case PHONE_CALL_TYPE:
                this.type = PHONE_CALL_TYPE;
                break;
            default:
                this.type = SMS_AND_PHONE_CALL_TYPE;
                break;
        }

        return singleton;
    }


    public int getType() {
        return type;
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

        switch (type) {
            case SMS_AND_PHONE_CALL_TYPE:
                startPhoneCallBroadcastReceiver();
                startSmsBroadcastReceiver();
                break;
            case SMS_TYPE:
                startSmsBroadcastReceiver();
                break;
            case PHONE_CALL_TYPE:
                startPhoneCallBroadcastReceiver();
                break;
        }
    }


    /**
     * Stop a previously started {@link android.content.BroadcastReceiver}
     */
    public void stop() {
        Log.d(TAG, "stop: hit");

        if (smsBroadcastReceiver != null) {
            context.unregisterReceiver(smsBroadcastReceiver);
            smsBroadcastReceiver = null;
        }

        if(phoneCallBroadcastReceiver != null) {
            context.unregisterReceiver(phoneCallBroadcastReceiver);
            phoneCallBroadcastReceiver = null;
        }
    }


    public void clearGatewayList() {
        Log.d(TAG, "clearGatewayList: hit");

        if (gatewayList != null) {
            gatewayList.clear();
        }
    }


    /**
     * Start a {@link SmsBroadcastReceiver} and attach
     * a listener for results.
     */
    private void startSmsBroadcastReceiver() {
        Log.d(TAG, "startSmsBroadcastReceiver: hit");

        if (smsBroadcastReceiver == null) {
            smsBroadcastReceiver = new SmsBroadcastReceiver();
        }

        smsBroadcastReceiver.setSmsBroadcastListener(new SmsBroadcastListener() {
            @Override
            public void onMessagesReceived(List<SmsMessage> smsMessageList) {
                for (SmsMessage smsMessage : smsMessageList) {
                    String smsNumber = smsMessage.getDisplayOriginatingAddress();
                    if (!Is.empty(smsNumber)) {
                        Log.d(TAG, "onMessagesReceived: " + smsNumber);

                        boolean isKnownGateway = isKnownGateway(smsNumber);

                        if (!isKnownGateway) {
                            Log.i(TAG, "onMessagesReceived: not interesting number");
                            continue;
                        }

                        if (smsListener == null) {
                            Log.i(TAG, "onMessagesReceived: smsListener is NULL");
                            continue;
                        }

                        smsListener.onSmsReceived(smsMessage);
                    }
                }
            }
        });


        IntentFilter intentFilter = new IntentFilter(RECEIVED_SMS_BROADCAST);

        context.registerReceiver(smsBroadcastReceiver, intentFilter);
    }


    /**
     * Start a {@link PhoneCallBroadcastReceiver} and attach
     * a listener for results.
     */
    private void startPhoneCallBroadcastReceiver() {
        Log.d(TAG, "startPhoneCallBroadcastReceiver: hit");

        if (phoneCallBroadcastReceiver == null) {
            phoneCallBroadcastReceiver = new PhoneCallBroadcastReceiver();
        }

        phoneCallBroadcastReceiver.setPhoneCallBroadcastListener(new PhoneCallBroadcastListener() {
            @Override
            public void onCallReceived(String phoneNumber) {
                Log.d(TAG, "onCallReceived: " + phoneNumber);

                if (phoneCallListener == null) {
                    Log.i(TAG, "onCallReceived: phoneCallListener is NULL");
                    return;
                }

                phoneCallListener.onPhoneCallReceived(phoneNumber);
            }
        });

        IntentFilter intentFilter = new IntentFilter(PHONE_STATE_BROADCAST);

        context.registerReceiver(phoneCallBroadcastReceiver, intentFilter);
    }


    /**
     * Determine if the given number is part of the gateways
     * for which we care about.
     *
     * @param smsNumber
     * @return TRUE if the number is part of important gateways of us.
     * FALSE otherwise.
     */
    private boolean isKnownGateway(String smsNumber) {
        for (String gateway : gatewayList) {
            if (gateway.trim().toLowerCase().equals(smsNumber.trim().toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
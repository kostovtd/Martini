package com.kostovtd.martini;

import android.telephony.SmsMessage;

import java.util.List;

/**
 * Created by kostovtd on 30.05.17.
 */

public interface BroadcastListener {

    void onMessagesReceived(List<SmsMessage> smsMessageList);

}

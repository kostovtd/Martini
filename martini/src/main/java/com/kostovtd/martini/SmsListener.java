package com.kostovtd.martini;

import android.telephony.SmsMessage;

/**
 * Created by kostovtd on 25.05.17.
 */

public interface SmsListener {

    void onSmsReceived(SmsMessage smsMessage);

}

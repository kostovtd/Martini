package com.kostovtd.martini;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

/**
 * Created by kostovtd on 02.06.17.
 */

@RunWith(RobolectricTestRunner.class)
public class SmsBroadcastReceiverTest {

    @Mock
    Context context;


    static {
        ShadowLog.stream = System.out;
    }


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testBroadcastReceiverRegistered() {
        // start and register our broadcast receiver
        Martini.with(context).addGateway("123").start();

        // get all registered receivers
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

        Assert.assertFalse(registeredReceivers.isEmpty());
    }
}

package com.kostovtd.martini;

import android.content.Context;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kostovtd on 01.06.17.
 */

@RunWith(RobolectricTestRunner.class)
public class MartiniTest {

    private static final String TAG = MartiniTest.class.getSimpleName();

    private Context context;


    static {
        ShadowLog.stream = System.out;
    }


    @Before
    public void setup() {
        Log.d(TAG, "setup: hit");

        MockitoAnnotations.initMocks(this);
        context = ShadowApplication.getInstance().getApplicationContext();
    }


    @Test
    public void obtainingMartiniInstanceWithContextShouldReturnSameInstance() {
        Log.d(TAG, "obtainingMartiniInstanceWithContextShouldReturnSameInstance: hit");

        Assert.assertSame(Martini.with(context), Martini.with(context));
    }


    @Test
    public void addingValidGatewayShouldIncludeNewGateway() {
        Log.d(TAG, "addingValidGatewayShouldIncludeNewGateway: hit");

        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();
        Martini.with(context).addGateway("1234");

        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayShouldNotIncludeNewGateway() {
        Log.d(TAG, "addingEmptyGatewayShouldNotIncludeNewGateway: hit");

        Martini.with(context).clearGatewayList();

        int gatewayListSize = Martini.with(context).getGatewayList().size();

        Martini.with(context).addGateway("");
        Assert.assertEquals(gatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayArrShouldNotIncludeNewGateways() {
        Log.d(TAG, "addingEmptyGatewayArrShouldNotIncludeNewGateways");

        Martini.with(context).clearGatewayList();

        String[] gatewaysArr = {};
        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        Martini.with(context).addGateways(gatewaysArr);
        Assert.assertEquals(oldGatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingValidGatewayArrShouldIncludeNewGateways() {
        Log.d(TAG, "addingValidGatewayArrShouldIncludeNewGateways: hit");

        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();
        String[] gatewayArr = {"123", "432", "121212"};

        Martini.with(context).addGateways(gatewayArr);
        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayArrayListShouldNowIncludeNewGateways() {
        Log.d(TAG, "addingEmptyGatewayArrayListShouldNowIncludeNewGateways: hit");

        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        ArrayList<String> gatewayList = new ArrayList<>();
        Martini.with(context).addGateways(gatewayList);

        Assert.assertEquals(oldGatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingValidGatewayArrayListShouldIncludeNewGateways() {
        Log.d(TAG, "addingValidGatewayArrayListShouldIncludeNewGateways: hit");

        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        ArrayList<String> gatewayList = new ArrayList<>();
        gatewayList.add("123");
        gatewayList.add("321");
        gatewayList.add("5435");

        Martini.with(context).addGateways(gatewayList);

        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }


    @Test
    public void testBroadcastReceiverRegistered() {
        Log.d(TAG, "testBroadcastReceiverRegistered: hit");

        // start and register our broadcast receiver
        Martini.with(context).addGateway("123").start();

        // get all registered receivers
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

        // test if the BroadcastReceiver was registered successfully
        Assert.assertFalse(registeredReceivers.isEmpty());
    }


    @Test
    public void testBroadcastReceiverUnregistered() {
        Log.d(TAG, "testBroadcastReceiverUnregistered: hit");

        // start and register our broadcast receiver
        Martini.with(context).addGateway("123").start();

        // stop and unregister our BroadcastReceiver
        Martini.with(context).stop();

        // get all registered receivers
        List<ShadowApplication.Wrapper> registeredReceivers = ShadowApplication.getInstance().getRegisteredReceivers();

        // test if the BroadcastReceiver was stopped successfully
        Assert.assertTrue(registeredReceivers.isEmpty());
    }


    @Test
    public void testMartiniTypeWithValidValueShouldChangeType() {
        Log.d(TAG, "testMartiniTypeWithValidValueShouldChangeType: hit");

        // test PHONE_CALL_TYPE
        Martini.with(context).setType(Martini.PHONE_CALL_TYPE);
        int martiniType = Martini.with(context).getType();
        Assert.assertEquals(Martini.PHONE_CALL_TYPE, martiniType);

        // test SMS_TYPE
        Martini.with(context).setType(Martini.SMS_TYPE);
        martiniType = Martini.with(context).getType();
        Assert.assertEquals(Martini.SMS_TYPE, martiniType);

        // test SMS_AND_PHONE_CALL_TYPE
        Martini.with(context).setType(Martini.SMS_AND_PHONE_CALL_TYPE);
        martiniType = Martini.with(context).getType();
        Assert.assertEquals(Martini.SMS_AND_PHONE_CALL_TYPE, martiniType);
    }


    @Test
    public void testMartiniTypeWithInvalidValueShouldSetTypeToSmsAndCall() {
        Log.d(TAG, "testMartiniTypeWithInvalidValueShouldSetTypeToSmsAndCall: hit");

        Martini.with(context).setType(12345); // a random value
        int martiniType = Martini.with(context).getType();
        Assert.assertEquals(Martini.SMS_AND_PHONE_CALL_TYPE, martiniType);
    }
}

package com.kostovtd.martini;

import android.content.Context;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;


/**
 * Created by kostovtd on 01.06.17.
 */

@RunWith(RobolectricTestRunner.class)
public class MartiniTest {

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
    public void obtainingMartiniInstanceWithContextShouldReturnSameInstance() {
        Assert.assertSame(Martini.with(context), Martini.with(context));
    }


    @Test
    public void addingValidGatewayShouldIncludeNewGateway() {
        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();
        Martini.with(context).addGateway("1234");

        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayShouldNotIncludeNewGateway() {
        Martini.with(context).clearGatewayList();

        int gatewayListSize = Martini.with(context).getGatewayList().size();

        Martini.with(context).addGateway("");
        Assert.assertEquals(gatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayArrShouldNotIncludeNewGateways() {
        Martini.with(context).clearGatewayList();

        String[] gatewaysArr = {};
        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        Martini.with(context).addGateways(gatewaysArr);
        Assert.assertEquals(oldGatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingValidGatewayArrShouldIncludeNewGateways() {
        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();
        String[] gatewayArr = {"123", "432", "121212"};

        Martini.with(context).addGateways(gatewayArr);
        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingEmptyGatewayArrayListShouldNowIncludeNewGateways() {
        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        ArrayList<String> gatewayList = new ArrayList<>();
        Martini.with(context).addGateways(gatewayList);

        Assert.assertEquals(oldGatewayListSize, Martini.with(context).getGatewayList().size());
    }


    @Test
    public void addingValidGatewayArrayListShouldIncludeNewGateways() {
        Martini.with(context).clearGatewayList();

        int oldGatewayListSize = Martini.with(context).getGatewayList().size();

        ArrayList<String> gatewayList = new ArrayList<>();
        gatewayList.add("123");
        gatewayList.add("321");
        gatewayList.add("5435");

        Martini.with(context).addGateways(gatewayList);

        Assert.assertTrue(oldGatewayListSize < Martini.with(context).getGatewayList().size());
    }
}

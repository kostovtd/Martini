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

import java.util.List;


/**
 * Created by kostovtd on 01.06.17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class MartiniTest {

    @Mock
    Context context;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void obtainingMartiniInstanceWithContextShouldReturnSameInstance() {
        PowerMockito.mockStatic(Log.class);
        Assert.assertSame(Martini.with(context), Martini.with(context));
    }


    @Test
    public void addingEmptyGatewayIntoEmptyListShouldNotIncludeNewGateway() {
        PowerMockito.mockStatic(Log.class);
        Martini.with(context).addGateway("");
        List<String> gatewayList = Martini.with(context).getGatewayList();
        Assert.assertNull(gatewayList);
    }


    @Test
    public void addingValidGatewayIntoEmptyListShouldReturnNotNullList() {
        PowerMockito.mockStatic(Log.class);
        Martini.with(context).addGateway("1234");
        Assert.assertNotNull(Martini.with(context).getGatewayList());
    }


    @Test
    public void addingEmptyGatewayIntoFullListShouldNotIncludeNewGateway() {
        PowerMockito.mockStatic(Log.class);
        Martini.with(context).addGateway("12314");
        int gateListSize = Martini.with(context).getGatewayList().size();

        Martini.with(context).addGateway("");
        Assert.assertEquals(gateListSize, Martini.with(context).getGatewayList().size());
    }
}

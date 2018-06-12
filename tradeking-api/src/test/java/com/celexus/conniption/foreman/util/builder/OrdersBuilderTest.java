package com.celexus.conniption.foreman.util.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.celexus.conniption.foreman.util.ResponseFormat;

public class OrdersBuilderTest {

    @Test
    public void getOrdersTest() {
        APIBuilder b = OrdersBuilder.getOrders("XXX", ResponseFormat.XML);
        assertTrue("Parameters are not empty!", b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/XXX/orders.xml",
                b.getResourceURL());
    }

    @Test
    public void postOrdersTest() {
        APIBuilder b = OrdersBuilder.postOrder("XXX", "bogusFIXML", ResponseFormat.XML);
        assertTrue("Parameters are not empty!", b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/XXX/orders.xml",
                b.getResourceURL());
        assertEquals("Request Payload different", "bogusFIXML", b.getBody());
    }

    @Test
    public void postPreviewOrderTest() {
        APIBuilder b = OrdersBuilder.preview("XXX", "bogusFIXML", ResponseFormat.XML);
        assertTrue("Parameters are not empty!", b.getParameters().isEmpty());
        assertEquals(
                "Resource URL different",
                "https://api.tradeking.com/v1/accounts/XXX/orders/preview.xml",
                b.getResourceURL());
        assertEquals("Request Payload different", "bogusFIXML", b.getBody());
    }
}

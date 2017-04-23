package com.timedtester;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Holder;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.timedtester.TestClassRequest;
import org.timedtester.TestClassResponse;
import org.timedtester.TimedTesterWSCallback;
import org.timedtester.WsAsyncCallback;
import org.timedtester.WsGetTestReportCallback;
import org.timedtester.WsTestReportCallback;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

/**
 *
 * @author viga0114
 */
public class GeneralTask implements Runnable {
    
    String callbackAddress;
    String callerId = "test";
    
    public GeneralTask(TestClassRequest body, 
            EndpointReferenceType replyToHeader) {
        final String callbackAddress = replyToHeader.getAddress().getValue();
        System.out.println("callbackAddress = " + callbackAddress);
        this.callbackAddress = callbackAddress;        
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneralTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        callback();
    }
    
    private void callback() {
        final EndpointReference callbackEndPoint = 
                new W3CEndpointReferenceBuilder().address(callbackAddress).build();        
        TimedTesterWSCallback callBack = new TimedTesterWSCallback();

        WsGetTestReportCallback wsGetTestReportCallback = new WsGetTestReportCallback();
        wsGetTestReportCallback.setMessage("test");
        
        WsTestReportCallback cb = callBack.getPort(callbackEndPoint, WsTestReportCallback.class);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.ws.connect.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.ws.request.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.internal.ws.connect.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.internal.ws.request.timeout", 1000);

        cb.getTestReportCallback(wsGetTestReportCallback);
        
    }
}

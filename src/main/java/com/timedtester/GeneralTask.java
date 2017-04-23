package com.timedtester;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.timedtester.MethodAndTime;
import org.timedtester.TestClassRequest;
import org.timedtester.TestClassResult;
import org.timedtester.TimedTesterWSCallback;
import org.timedtester.WsGetTestReportCallback;
import org.timedtester.WsTestReportCallback;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

/**
 *
 * @author Victor
 */
public class GeneralTask implements Runnable {
    
    String callbackAddress;
    String callerId = "test";
    String className;
    List<String> methods;
    
    public GeneralTask(
            TestClassRequest body,
            EndpointReferenceType replyToHeader
    ) {
        final String callbackAddress = replyToHeader.getAddress().getValue();
        System.out.println("callbackAddress = " + callbackAddress);
        this.callbackAddress = callbackAddress;
        this.className = body.getClassName();
        this.methods = body.getMethods();
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
        TestClassResult result = new TestClassResult();
        result.setClassName("testClass");
        MethodAndTime firstTestMethod = new MethodAndTime();
        firstTestMethod.setMethod("testMethod");
        firstTestMethod.setTime(100.0);
        result.getMethodAndTime().add(firstTestMethod);
        MethodAndTime secondTestMethod = new MethodAndTime();
        secondTestMethod.setMethod("secondMethod");
        secondTestMethod.setTime(10.0);
        result.getMethodAndTime().add(secondTestMethod);
        wsGetTestReportCallback.setResult(result);
        
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

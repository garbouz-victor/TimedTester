package com.timedtester;

import com.timedtester.lib.tests.predefined.PredefinedLinkedListTests;
import com.timedtester.lib.tests.predefined.PredefinedListTests;
import com.timedtester.lib.tests.predefined.PredefinedTests;
import com.timedtester.lib.utils.data.Tuple;
import com.timedtester.util.ResultTransformer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
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
    
    private static final Log LOG = LogFactory.getLog(GeneralTask.class);
    
    String callbackAddress;
    String callerId;
    String className;
    List<String> methods;
    
    private static final List<PredefinedTests> predefinedTests = new ArrayList<>();
    
    static {
        predefinedTests.add(PredefinedListTests.getInstance());
        predefinedTests.add(PredefinedLinkedListTests.getInstance());
    }
    
    public GeneralTask(
            TestClassRequest body,
            EndpointReferenceType replyToHeader
    ) {
        final String callbackAddress = replyToHeader.getAddress().getValue();
        if (LOG.isDebugEnabled()) {
            LOG.debug("callbackAddress = " + callbackAddress);
        }        
        this.callbackAddress = callbackAddress;
        this.className = body.getClassName();
        this.methods = body.getMethods();
    }

    @Override
    public void run() {
        final WsGetTestReportCallback testReport;
        try {
            Class receivedClass = Class.forName(className);
            testReport = new WsGetTestReportCallback();
            for (PredefinedTests test : predefinedTests) {
                if (test.getTestClass()
                        .isAssignableFrom(receivedClass)) {
                    Map<String, List<Tuple<String, String>>> report = test.runTests(receivedClass);
                    TestClassResult result = ResultTransformer.transform(report);
                    testReport.setResult(result);
                    testReport.getResult().setClassName(className);
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.error(ex);
            throw new RuntimeException(ex);
        }
        callback(testReport);
    }
    
    private void callback(WsGetTestReportCallback report) {
        final EndpointReference callbackEndPoint = 
                new W3CEndpointReferenceBuilder().address(callbackAddress).build();        
        TimedTesterWSCallback callBack = new TimedTesterWSCallback();
        
        WsTestReportCallback cb = callBack.getPort(callbackEndPoint, WsTestReportCallback.class);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.ws.connect.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.ws.request.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.internal.ws.connect.timeout", 1000);
        ((BindingProvider) cb).getRequestContext().put(
                "com.sun.xml.internal.ws.request.timeout", 1000);

        cb.getTestReportCallback(report);
        
    }
}

package com.timedtester;

import javax.jws.WebService;
import org.timedtester.TestClassRequest;
import org.timedtester.TestClassResponse;
import org.timedtester.TimedTesterWS;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

/**
 *
 * @author Victor
 */
@WebService(serviceName = "TimedTesterWS", 
        portName = "TimedTesterWSPort", 
        endpointInterface = "org.timedtester.TimedTesterWS", 
        targetNamespace = "http://timedtester.org/",
        wsdlLocation = "WEB-INF/wsdl/service.wsdl")
public class TimedTester implements TimedTesterWS {

    @Override
    public TestClassResponse testClass(
            TestClassRequest body, 
            EndpointReferenceType replyToHeader) {
        GeneralTask task = new GeneralTask(body, replyToHeader);
        Thread th = new Thread(task);
        th.start();
        return new TestClassResponse();
    }
    
}

package com.timedtester;

import javax.jws.WebService;

/**
 *
 * @author Victor
 */
@WebService(serviceName = "TimedTesterWS", 
        portName = "TimedTesterWSPort", 
        endpointInterface = "org.timedtester.TimedTesterWS", 
        targetNamespace = "http://timedtester.org/",
        wsdlLocation = "WEB-INF/wsdl/service.wsdl")
public class TimedTester {

    public int add(int i, int j) {
        return i + j;
    }
    
}

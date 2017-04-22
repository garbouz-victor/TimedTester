package com.timedtester;

import javax.jws.WebService;

/**
 *
 * @author Victor
 */
@WebService(serviceName = "CalculatorWS", 
        portName = "CalculatorWSPort", 
        endpointInterface = "org.me.calculator.CalculatorWS", 
        targetNamespace = "http://calculator.me.org/",
        wsdlLocation = "WEB-INF/wsdl/service.wsdl")
public class TimedTester {

    public int add(int i, int j) {
        return i + j;
    }
    
}

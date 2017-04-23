package com.timedtester;

import javax.jws.WebService;
import org.timedtester.Add;
import org.timedtester.AddAsync;
import org.timedtester.AddAsyncResponse;
import org.timedtester.AddResponse;
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

//    @Override
//    public int add(int i, int j) {
//        return i + j;
//    }
//    
//    @Override
//    public String addAsync(int i, int j) {
//        return "received " + i + " " + j;
//    }

    @Override
    public TestClassResponse testClass(
            TestClassRequest body, 
            EndpointReferenceType replyToHeader) {
        GeneralTask task = new GeneralTask(body, replyToHeader);
        Thread th = new Thread(task);
        th.start();
        return new TestClassResponse();
    }

    @Override
    public AddResponse add(Add parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AddAsyncResponse addAsync(AddAsync parameters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}

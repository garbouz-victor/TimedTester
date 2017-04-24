package com.timedtester.util;

import com.timedtester.lib.utils.data.Tuple;
import java.util.List;
import java.util.Map;
import org.timedtester.MethodAndTime;
import org.timedtester.TestClassResult;

/**
 *
 * @author Victor
 */
public class ResultTransformer {
    public static TestClassResult transform(
            Map<String, List<Tuple<String, String>>> report
    ) {
        TestClassResult result = new TestClassResult();
        for (String methodName : report.keySet()) {
            for (Tuple<String, String> methodReport : report.get(methodName)) {
                MethodAndTime methodAndTime = new MethodAndTime();
                methodAndTime.setMethod(methodName);
                methodAndTime.setElementsCount(methodReport.getFirst());
                methodAndTime.setTime(methodReport.getSecond());
                result.getMethodAndTime().add(methodAndTime);
            }            
        }
        return result;
    }
}

package com.timedtester.lib.tests.predefined;

import com.timedtester.lib.containers.Test;
import com.timedtester.lib.containers.Tester;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class PredefinedTestsConsumer {
    
    public static void main(String... args) {
        try {
            PredefinedListTests pTests = PredefinedListTests.getInstance();
            if (pTests.getTestClass().isAssignableFrom(Class.forName("java.util.ArrayList"))) {
                Collection<? super Test> tests = pTests.getAllTests();
                Class testedClass = Class.forName("java.util.ArrayList");
                pTests.runTests(testedClass);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PredefinedTestsConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

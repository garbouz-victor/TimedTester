package com.timedtester.lib.tests.predefined;

import com.timedtester.lib.containers.Test;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 *
 * @author Victor
 */
public interface PredefinedTests {
    public Class<?> getTestClass();
    public Collection<Method> getTestDefinedMethods();
    public Collection<? super Test> getAllTests();
    public void runTests(Class c);
}

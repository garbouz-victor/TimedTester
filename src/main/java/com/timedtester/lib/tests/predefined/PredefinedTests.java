package com.timedtester.lib.tests.predefined;

import com.timedtester.lib.containers.Test;
import com.timedtester.lib.utils.data.Tuple;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor
 */
public interface PredefinedTests {
    public Class<?> getTestClass();
    public Collection<Method> getTestDefinedMethods();
    public Collection<? super Test> getAllTests();
    public Map<String, List<Tuple<String, String>>> runTests(Class c);
}

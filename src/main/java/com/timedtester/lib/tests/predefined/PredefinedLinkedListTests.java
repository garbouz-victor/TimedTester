package com.timedtester.lib.tests.predefined;

import com.timedtester.lib.containers.Test;
import com.timedtester.lib.containers.TestParam;
import com.timedtester.lib.containers.Tester;
import com.timedtester.lib.utils.CountingIntegerList;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class PredefinedLinkedListTests implements PredefinedTests {
    private final Map<Method, ? super Test> testsAndMethods = new HashMap<>();
    private static volatile PredefinedLinkedListTests instance;
    
    private PredefinedLinkedListTests() {
        try {
            testsAndMethods.put(LinkedList.class.getMethod("addFirst", Object.class), new Test<LinkedList<Integer>>("addFirst") {
                @Override
                public int test(LinkedList<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int size = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        for (int j = 0; j < size; j++) {
                            list.addFirst(20);
                        }
                    }
                    return loops * size;
                }
            });

            testsAndMethods.put(LinkedList.class.getMethod("addLast", Object.class), new Test<LinkedList<Integer>>("addLast") {
                @Override
                public int test(LinkedList<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int size = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        for (int j = 0; j < size; j++) {
                            list.addLast(20);
                        }
                    }
                    return loops * size;
                }
            });

            testsAndMethods.put(LinkedList.class.getMethod("removeFirst"), new Test<LinkedList<Integer>>("rmFirst") {
                @Override
                public int test(LinkedList<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int size = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        list.addAll(new CountingIntegerList(size));
                        while (list.size() > 0) {
                            list.removeFirst();
                        }
                    }
                    return loops * size;
                }
            });

            testsAndMethods.put(LinkedList.class.getMethod("removeLast"), new Test<LinkedList<Integer>>("rmLast") {
                @Override
                public int test(LinkedList<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int size = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        list.addAll(new CountingIntegerList(size));
                        while (list.size() > 0) {
                            list.removeLast();
                        }
                    }
                    return loops * size;
                }
            });
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(PredefinedLinkedListTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static PredefinedLinkedListTests getInstance() {
        if (instance == null) {
            synchronized(PredefinedLinkedListTests.class) {
                if (instance == null) {
                    instance = new PredefinedLinkedListTests();
                }
            }
        }
        return instance;
    }
    
    @Override
    public Class<?> getTestClass() {
        return LinkedList.class;
    }

    @Override
    public Collection<Method> getTestDefinedMethods() {
        return testsAndMethods.keySet();
    }

    @Override
    public Collection<? super Test> getAllTests() {
        return testsAndMethods.values();
    }

    @Override
    public void runTests(Class className) {
        Tester tester;
        try {
            tester = new Tester(className.newInstance(), testsAndMethods.values());
            tester.timedTest();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(PredefinedLinkedListTests.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}

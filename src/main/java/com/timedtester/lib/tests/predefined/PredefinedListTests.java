package com.timedtester.lib.tests.predefined;

import com.timedtester.lib.containers.Test;
import com.timedtester.lib.containers.TestParam;
import com.timedtester.lib.containers.Tester;
import com.timedtester.lib.utils.CountingIntegerList;
import com.timedtester.lib.utils.data.Tuple;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Victor
 */
public class PredefinedListTests implements PredefinedTests {
    private Random rand = new Random();
    private int reps = 1000;
    private Map<Method, ? super Test> testsAndMethods = new HashMap<>();
    private static volatile PredefinedListTests instance;
    private static final Log LOG = LogFactory.getLog(PredefinedLinkedListTests.class);
    
    private PredefinedListTests() {
        try {
            testsAndMethods.put(List.class.getMethod("add", Object.class), new Test<List<Integer>>("add") {
                @Override
                public int test(List<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int listSize = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        for (int j = 0; j < listSize; j++) {
                            list.add(j);
                        }
                    }
                    return loops * listSize;
                }
            });

            testsAndMethods.put(List.class.getMethod("get", int.class), new Test<List<Integer>>("get") {
                @Override
                public int test(List<Integer> list, TestParam tp) {
                    int loops = tp.loops * reps;
                    int listSize = list.size();
                    for (int i = 0; i < loops; i++) {
                        list.get(rand.nextInt(listSize));
                    }
                    return loops;
                }
            });

            testsAndMethods.put(List.class.getMethod("set", int.class, Object.class), new Test<List<Integer>>("set") {
                @Override
                public int test(List<Integer> list, TestParam tp) {
                    int loops = tp.loops * reps;
                    int listSize = list.size();
                    for (int i = 0; i < loops; i++) {
                        list.set(rand.nextInt(listSize), 20);
                    }
                    return loops;
                }
            });

            testsAndMethods.put(ListIterator.class.getMethod("add", Object.class), new Test<List<Integer>>("iteradd") {
                @Override
                public int test(List<Integer> list, TestParam tp) {
                    final int LOOPS = 1000000;
                    int half = list.size() / 2;
                    ListIterator<Integer> it = list.listIterator(half);
                    for (int i = 0; i < LOOPS; i++) {
                        it.add(20);
                    }
                    return LOOPS;
                }
            });

            testsAndMethods.put(List.class.getMethod("add", int.class, Object.class),
                    new Test<List<Integer>>("insert") {
                        @Override
                        public int test(List<Integer> list, TestParam tp) {
                            int loops = tp.loops;
                            for (int i = 0; i < loops; i++) {
                                list.add(5, 20);
                            }
                            return loops;
                        }
                    });

            testsAndMethods.put(List.class.getMethod("remove", int.class), new Test<List<Integer>>("remove") {
                @Override
                public int test(List<Integer> list, TestParam tp) {
                    int loops = tp.loops;
                    int size = tp.size;
                    for (int i = 0; i < loops; i++) {
                        list.clear();
                        list.addAll(new CountingIntegerList(size));
                        while (list.size() > 5) {
                            list.remove(5);
                        }
                    }
                    return loops * size;
                }
            });

        } catch (NoSuchMethodException | SecurityException ex) {
            LOG.error(ex);
        }
    }

    public static PredefinedListTests getInstance() {
        if (instance == null) {
            synchronized(PredefinedListTests.class) {
                if (instance == null) {
                    instance = new PredefinedListTests();
                }
            }
        }
        return instance;
    }
    
    @Override
    public Class<?> getTestClass() {
        return List.class;
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
    public Map<String, List<Tuple<String, String>>> runTests(Class className) {
        try {
            return ListTester.run((List<Integer>) className.newInstance(), getAllTests());
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error(ex);
            throw new RuntimeException(ex);
        }
    }
    
    static class ListTester extends Tester {

        public ListTester(List<Integer> container, 
                Collection<? super Test> tests) {
            super(container, tests);
        }
        
        @Override
        protected List<Integer> initialize(int size) {
            ((List<Integer>)container).clear();
            ((List<Integer>)container).addAll(new CountingIntegerList(size));
            return (List<Integer>) container;
        }
        
        public static Map<String, List<Tuple<String, String>>> run(List<Integer> list,
                Collection<? super Test> tests) {
            return new ListTester(list, tests).timedTest();
        }
    }
}

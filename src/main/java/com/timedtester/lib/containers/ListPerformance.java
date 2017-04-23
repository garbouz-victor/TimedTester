package com.timedtester.lib.containers;

import com.timedtester.lib.utils.CountingGenerator;
import com.timedtester.lib.utils.CountingIntegerList;
import com.timedtester.lib.utils.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class ListPerformance {
    static Random rand = new Random();
    static int reps = 1000;
    static List<Test<List<Integer>>> tests = new ArrayList<>();
    static List<Test<LinkedList<Integer>>> qTests = new ArrayList<>();
    static {
        tests.add(new Test<List<Integer>>("add") {
            @Override
            public int test(List<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int listSize = tp.size;
                for (int i = 0; i < loops; i++) {
                    list.clear();
                    for(int j = 0; j < listSize; j++) {
                        list.add(j);
                    }
                }
                return loops * listSize;
            }
        }); 
        
        tests.add(new Test<List<Integer>>("get") {
            @Override
            public int test(List<Integer> list, TestParam tp) {
                int loops = tp.loops * reps;
                int listSize = list.size();
                for(int i = 0; i < loops; i++) {
                    list.get(rand.nextInt(listSize));
                }
                return loops;
            }            
        });
        
        tests.add(new Test<List<Integer>>("set") {
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
        
        tests.add(new Test<List<Integer>>("iteradd") {
            @Override
            public int test(List<Integer> list, TestParam tp) {
                final int LOOPS = 1000000;
                int half = list.size() / 2;
                ListIterator<Integer> it = list.listIterator(half);
                for(int i = 0; i < LOOPS; i++) {
                    it.add(20);
                }
                return LOOPS;
            }
        });
        
        tests.add(new Test<List<Integer>>("insert") {
            @Override
            public int test(List<Integer> list, TestParam tp) {
                int loops = tp.loops;
                for(int i = 0; i < loops; i++) {
                    list.add(5, 20);
                }
                return loops;
            }
        });
        
        tests.add(new Test<List<Integer>>("remove") {
            @Override
            public int test(List<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for (int i = 0; i < loops; i++) {
                    list.clear();
                    list.addAll(new CountingIntegerList(size));
                    while(list.size() > 5) {
                        list.remove(5);
                    }
                }
                return loops * size;
            }     
        });
        
        qTests.add(new Test<LinkedList<Integer>>("addFirst") {
            @Override
            public int test(LinkedList<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for(int i = 0; i < loops; i++) {
                    list.clear();
                    for(int j = 0; j < size; j++) {
                        list.addFirst(20);
                    }
                }
                return loops * size;
            }
        });
        
        qTests.add(new Test<LinkedList<Integer>>("addLast") {
            @Override
            public int test(LinkedList<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for(int i = 0; i < loops; i++) {
                    list.clear();
                    for(int j = 0; j < size; j++) {
                        list.addLast(20);
                    }
                }
                return loops * size;
            }
        });
        
        qTests.add(new Test<LinkedList<Integer>>("rmFirst") {
            @Override
            public int test(LinkedList<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for(int i = 0; i < loops; i++) {
                    list.clear();
                    list.addAll(new CountingIntegerList(size));
                    while(list.size() > 0) {
                        list.removeFirst();
                    }
                }
                return loops * size;
            }
        });
        
        qTests.add(new Test<LinkedList<Integer>>("rmLast") {
            @Override
            public int test(LinkedList<Integer> list, TestParam tp) {
                int loops = tp.loops;
                int size = tp.size;
                for(int i = 0; i < loops; i++) {
                    list.clear();
                    list.addAll(new CountingIntegerList(size));
                    while(list.size() > 0) {
                        list.removeLast();
                    }
                }
                return loops * size;
            }
        });
    }
    
    static class ListTester extends Tester<List<Integer>> {

        public ListTester(List<Integer> container, 
                List<Test<List<Integer>>> tests) {
            super(container, tests);
        }
        
        @Override
        protected List<Integer> initialize(int size) {
            container.clear();
            container.addAll(new CountingIntegerList(size));
            return container;
        }
        
        public static void run(List<Integer> list,
                List<Test<List<Integer>>> tests) {
            new ListTester(list, tests).timedTest();
        }
    }
    
    public static void main(String... args) {        
        if (args.length > 0) {
            Tester.defaultParams = TestParam.array(args);
        }
        Tester<List<Integer>> arrayTest = new Tester<List<Integer>>(null, tests.subList(1, 3)) {
            protected List<Integer> initialize(int size) {
                Integer[] ia = Generated.array(Integer.class, new CountingGenerator.Integer(), size);
                return Arrays.asList(ia);
            }
        };
        arrayTest.setHeadLine("Array as List");
        arrayTest.timedTest();
        Tester.defaultParams = TestParam.array(10, 5000, 100, 5000, 1000, 1000, 10000, 200);
        if (args.length > 0) {
            Tester.defaultParams = TestParam.array(args);
        }
        ListTester.run(new ArrayList<Integer>(), tests);
        ListTester.run(new LinkedList<Integer>(), tests);
        ListTester.run(new Vector<Integer>(), tests);
        Tester.fieldWidth = 12;
        Tester<LinkedList<Integer>> qTest = new Tester(new LinkedList<Integer>(), qTests);
        qTest.setHeadLine("Queue tests");
        qTest.timedTest();
    }
}

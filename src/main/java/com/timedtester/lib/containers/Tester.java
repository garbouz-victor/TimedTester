package com.timedtester.lib.containers;

import com.timedtester.lib.utils.data.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Victor
 * @param <C>
 */
public class Tester<C> {
    private static final Log LOG = LogFactory.getLog(Tester.class);
    public static int fieldWidth = 8;
    public static TestParam[] defaultParams = TestParam.array(
            10, 5000,
            100, 5000,
            1000, 5000,
            10000, 500
    );
    protected C initialize(int size) {return container;}
    protected C container;
    private String headline = "";
    private Collection<Test<C>> tests;
    private final Map<String, List<Tuple<String, String>>> report = new HashMap<>();
    Formatter formatter = new Formatter();
    
    private static String stringField() {
        return "%" + fieldWidth + "s";
    }
    
    private static String numberField() {
        return "%" + fieldWidth + "d";
    }
    
    private static int sizeWidth = 5;
    private static String sizeField = "%" + sizeWidth + "s";
    private TestParam[] paramList = defaultParams;
    
    public Tester(C container, Collection<Test<C>> tests) {
        this.container = container;
        this.tests = tests;
        if (container != null) {
            headline = container.getClass().getSimpleName();
        }
    }
    
    public Tester(C container, List<Test<C>> tests, TestParam[] paramList) {
        this(container, tests);
        this.paramList = paramList;
    }
    
    public void setHeadLine(String newHeadLine) {
        headline = newHeadLine;
    }
    
    public static <C> void run(C cntnr, List<Test<C>> tests) {
        new Tester<>(cntnr, tests).timedTest();
    }
    
    public static <C> void run(C cntr,
            List<Test<C>> tests, TestParam[] paramList) {
        new Tester<>(cntr, tests, paramList).timedTest();
    }
    
    private void displayHeader() {
        int width = fieldWidth * tests.size() + sizeWidth;
        int dashLength = width - headline.length() - 1;
        StringBuilder head = new StringBuilder(width);
        for (int i = 0; i < dashLength / 2; i++) {
            head.append('-');
        }
        head.append(' ');
        head.append(headline);
        head.append(' ');
        for(int i = 0; i < dashLength/2; i++) {
            head.append('-');
        }
        LOG.debug(head);
        LOG.debug(formatter.format(sizeField, "size"));
        for(Test test : tests) {
            LOG.debug(formatter.format(stringField(), test.name));
        }
        LOG.debug("\n");
    }
    
    public Map<String, List<Tuple<String, String>>> timedTest() {
        displayHeader();
        for(TestParam param : paramList) {
           LOG.debug(formatter.format(sizeField, param.size));
            for(Test<C> test : tests) {
                C kontainer = initialize(param.size);
                long start = System.nanoTime();
                int reps = test.test(kontainer, param);
                long duration = System.nanoTime() - start;
                long timePerRep = duration / reps;
                if (!report.containsKey(test.name)) {
                    report.put(test.name, new ArrayList<Tuple<String, String>>());
                }
                report.get(test.name).add(new Tuple(
                        String.valueOf(param.size), 
                        String.valueOf(timePerRep)
                ));
                LOG.debug(formatter.format(numberField(), timePerRep));
            }
            LOG.debug("\n");
        }
        return report;
    }
}

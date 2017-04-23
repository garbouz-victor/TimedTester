package com.timedtester.lib.containers;

/**
 *
 * @author Victor
 */
public abstract class Test<C> {
    String name;
    public Test(String name) { this.name = name; }
    public abstract int test(C container, TestParam tp);
}

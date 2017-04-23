package com.timedtester.lib.utils;

/**
 *
 * @author Victor
 */
public class CountingGenerator {
    public static class Integer implements Generator<java.lang.Integer> {
        private int value = 0;
        @Override
        public java.lang.Integer next() {
            return value++;
        }        
    }
}

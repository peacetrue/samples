package com.github.peacetrue.runnable_jar;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.printf("args: %s", Arrays.toString(args));
        System.out.println();
        int seconds = args.length == 0 ? Integer.MAX_VALUE : Integer.parseInt(args[0]);
        try {
            System.out.printf("seconds: %s", seconds);
            System.out.println();
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

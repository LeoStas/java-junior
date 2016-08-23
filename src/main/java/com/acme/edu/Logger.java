package com.acme.edu;

import java.util.Objects;

public class Logger {
    public static void log(int message) {
        printer("primitive: " + message);
    }

    public static void log(byte message) {
        printer("primitive: " + message);
    }

    public static void log(char message) {
        printer("char: " + message);
    }

    public static void log(String message) {
        printer("string: " + message);
    }

    public static void log(boolean message) {
        printer("primitive: " + message);
    }

    public static void log(Object message) {
        printer("reference: " + message);
    }

    private static void printer(String input){
        System.out.println(input);
    }

}

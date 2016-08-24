package com.acme.edu;

import java.util.Objects;

enum State
{
    NO_STATE,
    INT_STATE,
    STRING_STATE,
    BYTE_STATE,
    CHAR_STATE,
    BOOLEAN_STATE,
    OBJECT_STATE
}

public class Logger {
    private static int sum_int;
    private static byte sum_byte;
    private static String cur_string = "";
    private static int cntr;
    private static State state = State.NO_STATE;
    private static String buffer = "";

    private static void clearAll() {
        sum_int = 0;
        sum_byte = 0;
        cur_string = "";
        cntr = 0;
    }

    private static String stateToString(State state) {
        switch (state) {
            case BYTE_STATE:
            case INT_STATE:
            case BOOLEAN_STATE:
                return "primitive: ";
            case STRING_STATE:
                return "string: ";
            case OBJECT_STATE:
                return "reference: ";
            case CHAR_STATE:
                return "char: ";
            default:
                return "";
        }
    }

    private static void changeState(State newState) {
        if (state != State.NO_STATE && state != newState) {
            flush();
            buffer = stateToString(newState);
            clearAll();
        }
        state = newState;
    }

    private static void flush() {
        printer(buffer);
        buffer = "";
        clearAll();
    }

    public static void log(int message) {
        changeState(State.INT_STATE);
        boolean overflow = Integer.MAX_VALUE - sum_int < message;
        if (overflow) {
            flush();
            sum_int = Integer.MAX_VALUE;
        } else {
            sum_int += message;
        }
        buffer = buffer.concat("" + sum_int);
    }

    public static void closeLogSession() {
        flush();
    }



    public static void log(byte message) {
        changeState(State.BYTE_STATE);
        boolean overflow = Byte.MAX_VALUE - sum_byte < message;
        if (overflow) {
            buffer = buffer.concat("" + sum_byte);
            flush();
            sum_byte = Byte.MAX_VALUE;
        } else {
            sum_byte += message;
        }
    }

    public static void log(char message) {
        changeState(State.CHAR_STATE);
        buffer = buffer.concat("" + message);
        flush();
    }

    public static void log(String message) {
        changeState(State.STRING_STATE);
        ++cntr;
        if (!cur_string.equals(message)) {
            if (cntr > 1) {
                buffer = buffer.concat(message + " (x" + cntr + ")");
            } else {
                buffer = buffer.concat(message);
            }
            flush();
        }
        cur_string = message;

    }

    public static void log(boolean message) {
        changeState(State.BOOLEAN_STATE);
        buffer = buffer.concat("" + message);
        flush();
    }

    public static void log(Object message) {
        changeState(State.OBJECT_STATE);
        buffer = buffer.concat("" + message);
        flush();
    }

    private static void printer(String input){
        System.out.println(input);
    }

}

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
        buffer = "";
        state = State.NO_STATE;
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
            clearAll();
        }
        state = newState;
    }

    private static void flush() {
        printer(stateToString(state) + buffer);
        buffer = "";
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
        buffer = Integer.toString(sum_int);
    }

    public static void closeLogSession() {
        flush();
        clearAll();
    }

    public static void log(byte message) {
        changeState(State.BYTE_STATE);
        boolean overflow = Byte.MAX_VALUE - sum_byte < message;
        if (overflow) {
            flush();
            sum_byte = Byte.MAX_VALUE;
        } else {
            sum_byte += message;
        }
        buffer = Byte.toString(sum_byte);
    }

    public static void log(char message) {
        changeState(State.CHAR_STATE);
        buffer = Character.toString(message);
        flush();
        clearAll();
    }

    public static void log(String message) {
        changeState(State.STRING_STATE);
        if (!cur_string.isEmpty() && !cur_string.equals(message)) {
            flush();
            cntr = 0;
        }
        ++cntr;
        cur_string = message;
        String numStr = cntr > 1 ? " (x" + cntr + ")" : "";
        buffer = cur_string + numStr;
    }

    public static void log(boolean message) {
        changeState(State.BOOLEAN_STATE);
        buffer = Boolean.toString(message);
        flush();
        clearAll();
    }

    public static void log(Object message) {
        changeState(State.OBJECT_STATE);
        buffer = message.toString();
        flush();
        clearAll();
    }

    private static void printer(String input){
        System.out.println(input);
    }

}

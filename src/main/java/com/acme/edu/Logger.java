package com.acme.edu;

enum Type
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
    private static Decorator inDecorator = new NoDecorator();
    private static String buffer = "";

    private static void clearAll() {
        sum_int = 0;
        sum_byte = 0;
        cur_string = "";
        cntr = 0;
        buffer = "";
        inDecorator = new NoDecorator();
    }

    private static void changeState(Decorator decorator) {
        if (inDecorator.getType() != Type.NO_STATE && inDecorator.getType() != decorator.getType()) {
            flush();
            clearAll();
        }
        inDecorator = decorator;
        String str;
    }

    private static void flush() {
        printer(inDecorator.decorate(buffer));
        buffer = "";
    }

    public static void log(int message) {
        changeState(new IntDecorator());
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
        changeState(new ByteDecorator());
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
        changeState(new CharDecorator());
        buffer = Character.toString(message);
        flush();
        clearAll();
    }

    public static void log(String message) {
        changeState(new StringDecorator());
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
        changeState(new BooleanDecorator());
        buffer = Boolean.toString(message);
        flush();
        clearAll();
    }

    public static void log(Object message) {
        changeState(new ObjectDecorator());
        buffer = message.toString();
        flush();
        clearAll();
    }

    private static void printer(String input){
        System.out.println(input);
    }

}

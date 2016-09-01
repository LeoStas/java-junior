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
    private int sumInt;
    private byte sumByte;
    private String currentString = "";
    private int counter;
    private Decorator inDecorator = new NoDecorator();
    private String buffer = "";
    private Printer[] printers;

    public Logger(Printer ... printers) {
        this.printers = printers;
    }

    private void clearAll() {
        sumInt = 0;
        sumByte = 0;
        currentString = "";
        counter = 0;
        buffer = "";
        inDecorator = new NoDecorator();
    }

    private void changeStateAndDecoratorType(Decorator decorator) {
        if (inDecorator.getType() != Type.NO_STATE && inDecorator.getType() != decorator.getType()) {
            flush();
            clearAll();
        }
        inDecorator = decorator;
    }

    private void flush() {
        for (Printer printer : printers) {
            try {
                printer.print(inDecorator.decorate(buffer));
            } catch (SenderException e) {
                e.printStackTrace();
            }
        }
        buffer = "";
    }

    public void log(int message) {
        changeStateAndDecoratorType(new IntDecorator());
        boolean overflow = Integer.MAX_VALUE - sumInt < message;
        if (overflow) {
            flush();
            sumInt = Integer.MAX_VALUE;
        } else {
            sumInt += message;
        }
        buffer = Integer.toString(sumInt);
    }

    public void closeLogSession() {
        flush();
        for (Printer printer : printers) {
            try {
                printer.closePrinter();
            } catch (SenderException e) {
                e.printStackTrace();
            }
        }
        clearAll();
    }

    public void openLogSession() {
        for (Printer printer : printers) {
            try {
                printer.openPrinter();
            } catch (SenderException e) {
                e.printStackTrace();
            }
        }
        clearAll();
    }

    public void log(byte message) {
        changeStateAndDecoratorType(new ByteDecorator());
        boolean overflow = Byte.MAX_VALUE - sumByte < message;
        if (overflow) {
            flush();
            sumByte = Byte.MAX_VALUE;
        } else {
            sumByte += message;
        }
        buffer = Byte.toString(sumByte);
    }

    public void log(char message) {
        changeStateAndDecoratorType(new CharDecorator());
        buffer = Character.toString(message);
        flush();
        clearAll();
    }

    public void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException();
        }
        changeStateAndDecoratorType(new StringDecorator());
        if (!currentString.isEmpty() && !currentString.equals(message)) {
            flush();
            counter = 0;
        }
        ++counter;
        currentString = message;
        String numStr = counter > 1 ? " (x" + counter + ")" : "";
        buffer = currentString + numStr;
    }

    public void log(boolean message) {
        changeStateAndDecoratorType(new BooleanDecorator());
        buffer = Boolean.toString(message);
        flush();
        clearAll();
    }

    public void log(Object message) {
        if (message == null) {
            throw new IllegalArgumentException();
        }
        changeStateAndDecoratorType(new ObjectDecorator());
        buffer = message.toString();
        flush();
        clearAll();
    }


}

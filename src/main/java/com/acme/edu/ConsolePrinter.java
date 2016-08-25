package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class ConsolePrinter implements Printer {

    /**
     * @param msg       Message, that we need to print
     * @param decorator Decoration for message
     */
    @Override
    public void print(String msg, Decorator decorator) {
        System.out.println(decorator.decorate(msg));
    }
}

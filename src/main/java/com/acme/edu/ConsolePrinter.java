package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class ConsolePrinter implements Printer {

    /**
     * @param msg       Message, that we need to print
     *
     */
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }
}

package com.acme.edu;

import java.io.IOException;

/**
 * Created by Java_12 on 25.08.2016.
 */
public class ConsolePrinter implements Printer {

    /**
     * @param msg       Message, that we need to print
     *
     */
    @Override
    public void print(String msg) throws PrinterException {
        System.out.println(msg);
    }

    /**
     * Opens printer session
     *
     * @throws PrinterException
     */
    @Override
    public void openPrinter() throws PrinterException {
        System.out.flush();
    }

    /**
     * Closes printer session
     *
     * @throws PrinterException
     */
    @Override
    public void closePrinter() throws PrinterException {
        System.out.flush();
    }

}

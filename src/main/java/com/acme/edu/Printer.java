package com.acme.edu;

import java.io.Closeable;

/**
 * Created by Java_12 on 25.08.2016.
 */
public interface Printer {
    /**
     * @param msg Message, that we need to print
     *
     */
    public void print(String msg) throws PrinterException;

    /**
     * Opens printer session
     * @throws PrinterException
     */
    public void openPrinter() throws PrinterException;

    /** Closes printer session
     * @throws PrinterException
     */
    public void closePrinter() throws PrinterException;

}

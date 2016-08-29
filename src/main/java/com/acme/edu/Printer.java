package com.acme.edu;

import java.io.Closeable;

/**
 * Created by Java_12 on 25.08.2016.
 */
public interface Printer extends Closeable {
    /**
     * @param msg Message, that we need to print
     *
     */
    public void print(String msg) throws PrinterException;

}

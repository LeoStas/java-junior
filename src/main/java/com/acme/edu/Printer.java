package com.acme.edu;

/**
 * Created by Java_12 on 25.08.2016.
 */
public interface Printer {
    /**
     * @param msg Message, that we need to print
     *
     */
    public void print(String msg) throws SenderException;

    /**
     * Opens printer session
     * @throws SenderException
     */
    public void openPrinter() throws SenderException;

    /** Closes printer session
     * @throws SenderException
     */
    public void closePrinter() throws SenderException;

}

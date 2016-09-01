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
    public void print(String msg) throws SenderException {
        System.out.println(msg);
    }

    /**
     * Opens printer session
     *
     * @throws SenderException
     */
    @Override
    public void openPrinter() throws SenderException {
        System.out.flush();
    }

    /**
     * Closes printer session
     *
     * @throws SenderException
     */
    @Override
    public void closePrinter() throws SenderException {
        System.out.flush();
    }

}

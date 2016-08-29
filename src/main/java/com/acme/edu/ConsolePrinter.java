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
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     * <p>
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        System.out.flush();
        return;
    }
}

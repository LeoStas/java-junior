package com.acme.edu;

import java.io.*;

/**
 * Created by Java_12 on 30.08.2016.
 */
public class FilePrinter implements Printer {
    private File file;
    private OutputStreamWriter writer;

    public FilePrinter(File file) {
        this.file = file;
    }

    public FilePrinter(String filename) {
        this.file = new File(filename);
    }


    /**
     * @param msg Message, that we need to print
     */
    @Override
    public void print(String msg) throws SenderException {
        if (writer == null) {
            throw new SenderException("Printer not opened!");
        }
        try {
            writer.write(msg);
        } catch (IOException e) {
            throw new SenderException("Writing to file fails!", e);
        }
    }

    /**
     * Opens printer session
     *
     * @throws SenderException
     */
    @Override
    public void openPrinter() throws SenderException {
        try {
            writer = new OutputStreamWriter(
                        new BufferedOutputStream(
                            new FileOutputStream(file)), "UTF-8");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            throw new SenderException("File not found!", e);
        }
    }

    /**
     * Closes printer session
     *
     * @throws SenderException
     */
    @Override
    public void closePrinter() throws SenderException {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new SenderException("Close method failed", e);
            }
        }
    }
}

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
    public void print(String msg) throws PrinterException {
        if (writer == null) {
            throw new PrinterException("Printer not opened!");
        }
        try {
            writer.write(msg);
        } catch (IOException e) {
            throw new PrinterException("Writing to file fails!", e);
        }
    }

    /**
     * Opens printer session
     *
     * @throws PrinterException
     */
    @Override
    public void openPrinter() throws PrinterException {
        try {
            writer = new OutputStreamWriter(
                        new BufferedOutputStream(
                            new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new PrinterException("File not found!", e);
        }
    }

    /**
     * Closes printer session
     *
     * @throws PrinterException
     */
    @Override
    public void closePrinter() throws PrinterException {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new PrinterException("Close method failed", e);
            }
        }
    }
}

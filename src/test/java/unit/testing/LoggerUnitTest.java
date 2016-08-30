package unit.testing;

import com.acme.edu.Logger;
import com.acme.edu.Printer;
import com.acme.edu.PrinterException;
import com.acme.edu.SysoutCaptureAndAssertionAbility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.readLines;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;

/**
 * Created by Java_4 on 26.08.2016.
 */
public class LoggerUnitTest implements SysoutCaptureAndAssertionAbility {

    private Printer mockPrinter;
    private Logger logger;
    private File file = new File("test.txt");

    @Before
    public void setUp() {
        resetErr();
        captureSyserr();
        mockPrinter = mock(Printer.class);
        logger = new Logger ((Printer) mockPrinter);

    }

    @After
    public void resetAfter () {
        resetErr();
    }
    @Test
    public void shouldPrintCorrectStringForIntData () {

        logger.log(15);
        logger.closeLogSession();
        try {
            verify(mockPrinter).print("primitive: 15");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPrintCorrectStringForStringData () {

        logger.log("test data");
        logger.closeLogSession();
        try {
            verify(mockPrinter).print("string: test data");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPrintCorrectStringForCharData () {

        logger.log('a');
        logger.closeLogSession();
        try {
            verify(mockPrinter).print("char: a");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPrintCorrectStringForBooleanData () {

        logger.log(true);
        logger.closeLogSession();
        try {
            verify(mockPrinter).print("primitive: true");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPrintCorrectStringForObjectData () {

        Object obj = new Object();
        logger.log(obj);
        logger.closeLogSession();
        try {
            verify(mockPrinter).print("reference: " + obj.toString());
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldPrintCorrectStringForByteData () {

        logger.log((byte) 12);
        logger.closeLogSession();

        try {
            verify(mockPrinter).print("primitive: 12");
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForString () {
        logger.log ((String) null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionForObject () {
        logger.log((Object) null);
    }

    @Test
    public void shouldWriteStringDataToTheFileCorrectly () {
        //get file path and file name
        Logger loggerFile = new Logger ();
        boolean flag = false;

        //call logger.log
            //print to the file
        loggerFile.log("test data");
        loggerFile.closeLogSession();

        //read file from test

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("string: test data")){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);

        //assert that file contains correctdata

        //readLines

        //assertSame("data that's read from file is not");

    }

}

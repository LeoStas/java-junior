package unit.testing;

import com.acme.edu.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.commons.io.FileUtils.readLines;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Java_4 on 26.08.2016.
 */
public class LoggerUnitTest implements SysoutCaptureAndAssertionAbility {

    private Printer mockPrinter;
    private Logger logger;

    @Before
    public void setUp() {
        resetErr();
        captureSyserr();
        mockPrinter = mock(Printer.class);
        logger = new Logger ((Printer) mockPrinter);
        logger.openLogSession();

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

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        loggerFile.log("test data");
        loggerFile.closeLogSession();

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
    }

    @Test
    public void shouldWriteIntegerDataToTheFileCorrectly () {

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        loggerFile.log(288);
        loggerFile.closeLogSession();

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("primitive: 288")){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);
    }

    @Test
    public void shouldWriteByteDataToTheFileCorrectly () {

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        loggerFile.log((byte) 120);
        loggerFile.closeLogSession();

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("primitive: 120")){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);
    }

    @Test
    public void shouldWriteCharDataToTheFileCorrectly () {

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        loggerFile.log('a');
        loggerFile.closeLogSession();

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("char: a")){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);
    }

    @Test
    public void shouldWriteBooleanDataToTheFileCorrectly () {

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        loggerFile.log(true);
        loggerFile.closeLogSession();

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("primitive: true")){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);
    }

    @Test
    public void shouldWriteObjectDataToTheFileCorrectly () {

        File file = new File("test.txt");
        Logger loggerFile = new Logger (new FilePrinter(file));
        loggerFile.openLogSession();
        boolean flag = false;

        Object testObject = new Object();

        loggerFile.log(testObject);
        loggerFile.closeLogSession();

        try {
            List<String> FileData = readLines(file, "UTF-8");
            for (String s : FileData) {
                if (s.equals("reference: " + testObject.toString())){
                    flag = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(flag);
    }

}

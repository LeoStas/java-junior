package unit.testing;

import com.acme.edu.Logger;
import com.acme.edu.Printer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;

/**
 * Created by Java_4 on 26.08.2016.
 */
public class LoggerUnitTest {

    private Printer mockPrinter;
    private Logger logger;

    @Before
    public void setUp() {
        mockPrinter = mock(Printer.class);
        logger = new Logger ((Printer) mockPrinter);

    }
    @Test
    public void shouldPrintCorrectStringForIntData () {

        logger.log(15);
        logger.closeLogSession();

        verify(mockPrinter).print("primitive: 15");
    }

    @Test
    public void shouldPrintCorrectStringForStringData () {

        logger.log("test data");
        logger.closeLogSession();

        verify(mockPrinter).print("string: test data");
    }

    @Test
    public void shouldPrintCorrectStringForCharData () {

        logger.log('a');
        logger.closeLogSession();

        verify(mockPrinter).print("char: a");
    }

    @Test
    public void shouldPrintCorrectStringForBooleanData () {

        logger.log(true);
        logger.closeLogSession();

        verify(mockPrinter).print("primitive: true");
    }

    @Test
    public void shouldPrintCorrectStringForObjectData () {

        Object obj = new Object();
        logger.log(obj);
        logger.closeLogSession();

        verify(mockPrinter).print("reference: " + obj.toString());
    }

    @Test
    public void shouldPrintCorrectStringForByteData () {

        logger.log((byte) 12);
        logger.closeLogSession();

        verify(mockPrinter).print("primitive: 12");
    }
}

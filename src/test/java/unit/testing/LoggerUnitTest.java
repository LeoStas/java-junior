package unit.testing;

import com.acme.edu.Logger;
import com.acme.edu.Printer;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Java_4 on 26.08.2016.
 */
public class LoggerUnitTest {
    @Test
    public void shouldPrintCorrectString () {
        Printer mockPrinter = mock(Printer.class);

        Logger loggerForTesting = new Logger((Printer) mockPrinter);
        loggerForTesting.log(15);
        loggerForTesting.closeLogSession();
        verify(mockPrinter).print("primitive: 15");
    }


}

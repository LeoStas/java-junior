package com.acme.edu;

import com.acme.edu.Client;
import com.acme.edu.ExitClientException;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Java_4 on 01.09.2016.
 */
public class ClientTest implements SysoutCaptureAndAssertionAbility {

    @Test
    public void shouldSendCorrectMessage () throws ExitClientException, IOException {
        int port = 1111;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        String testMessage = new String("/snd this is my test message");
        Client client = new Client(port, host, mockClientSession);

            client.send(testMessage);
            verify(mockClientSession).sendMessage("this is my test message");

    }

    /*@Test
    public void shouldProcessWrongCommandsCorrectly() {
        int port = 1111;
        String host = "localhost";
        Client client = new Client (port, host, null);
        String testMessage = "abc123";
        try {
            client.send(testMessage);
            assertSysoutContains ("[WRONG INPUT]");
        } catch (ExitClientException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldNotSendEmptyMessages () {
        int port = 1111;
        String host = "localhost";
        Client client = new Client (port, host, null);
        String testMessage = "";
        try {
            client.send(testMessage);
            assertSysoutContains ("[EMPTY MESSAGE]");
        } catch (ExitClientException e) {
            e.printStackTrace();
        }
    }*/

    @Test(expected = ExitClientException.class)
    public void shouldProcessExitCommandCorrectly () throws ExitClientException {
        int port = 1111;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client client = new Client (port, host, mockClientSession);
        String testMessage = "/exit";
        client.send(testMessage);
    }


}

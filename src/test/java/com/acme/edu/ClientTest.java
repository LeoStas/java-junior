package com.acme.edu;

import com.acme.edu.Client;
import com.acme.edu.ExitClientException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientTest {

    @Before
    @After
    public void closeConnectionBeforeTest() {
        ClientSession ClientSession = new ClientSession(1111, "localhost");
        ClientSession.closeSession();
    }

    @After
    public void closeConnectionAfterTest() {
        ClientSession ClientSession = new ClientSession(1111, "localhost");
        ClientSession.closeSession();
    }



    @Test
    public void shouldSendCorrectMessage () throws ExitClientException, IOException {
        int port = 1111;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client client = new Client(port, host, mockClientSession);
        String testMessage = "/snd this is my test message";
        client.send(testMessage);
        verify(mockClientSession).sendMessage("this is my test message");

    }

    @Test
    public void shouldProcessWrongCommandsCorrectly() throws ExitClientException {
        int errorCodeReturned;
        int port = 1111;
        String host = "localhost";
        Client client = new Client (port, host);
        String testMessage = "/abc123";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-1, errorCodeReturned);
    }

    @Test
    public void shouldNotSendEmptyMessages () throws ExitClientException {
        int errorCodeReturned;
        int port = 1111;
        String host = "localhost";
        Client client = new Client (port, host);
        String testMessage = "/snd";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-2, errorCodeReturned);
    }

    @Test(expected = ExitClientException.class)
    public void shouldProcessExitCommandCorrectly () throws ExitClientException {
        int port = 1111;
        String host = "localhost";
        Client client = new Client (port, host);
        String testMessage = "/exit";
        client.send(testMessage);
    }

    @Test
    public void shouldProcessNonCommandConsoleInputCorrectly () throws ExitClientException {
        int port = 1111;
        int errorCodeReturned;
        String host = "localhost";
        Client client = new Client (port, host);
        String testMessage = "abc123";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-10, errorCodeReturned);
    }


}

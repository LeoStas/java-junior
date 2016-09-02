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

    @Test
    public void shouldProcessWrongCommandsCorrectly() throws ExitClientException {
        int port = 1111;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client mockClient = mock(Client.class);
        //Client client = new Client (port, host, mockClientSession);
        String testMessage = "/abc123";
        mockClient.send(testMessage);
        verify(mockClient).PrintErrorMessageToConsole("[WRONG COMMAND] Inapplicable command.");
    }

    @Test
    public void shouldNotSendEmptyMessages () throws ExitClientException {
        //int port = 1111;
        //String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client mockClient = mock(Client.class);
        //Client client = new Client (port, host, mockClientSession);
        String testMessage = "/snd";
        mockClient.send(testMessage);
        verify(mockClient).PrintErrorMessageToConsole("[EMPTY MESSAGE] Provide at least 1 character.");
    }

    @Test(expected = ExitClientException.class)
    public void shouldProcessExitCommandCorrectly () throws ExitClientException {
        int port = 1111;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client client = new Client (port, host, mockClientSession);
        String testMessage = "/exit";
        client.send(testMessage);
    }

    @Test
    public void shouldProcessNonCommandConsoleInputCorrectly () throws ExitClientException {
        ClientSession mockClientSession = mock(ClientSession.class);
        Client mockClient = mock(Client.class);
        //Client client = new Client (port, host, mockClientSession);
        String testMessage = "/snd";
        mockClient.send(testMessage);
        verify(mockClient).PrintErrorMessageToConsole("\"[WRONG INPUT] Your command contains a mistake.\" + System.lineSeparator() +\n" +
                "                    \"[WRONG INPUT] Your message should be separated from command with space.\"");
    }


}

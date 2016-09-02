package com.acme.edu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientTest implements SysoutCaptureAndAssertionAbility {
    private ExecutorService pool;
    private volatile Server server;
    @Before
    public void closeConnectionBeforeTest() {
        pool = Executors.newFixedThreadPool(1);
        pool.execute(() -> {
            try {
                server = new Server();
                server.runServer();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        ClientSession ClientSession = new ClientSession(666, "localhost");
        ClientSession.closeSession();
    }

    @After
    public void closeConnectionAfterTest() {
        ClientSession ClientSession = new ClientSession(666, "localhost");
        ClientSession.closeSession();
        server.shutdownServer();
        pool.shutdownNow();
    }



    @Test
    public void shouldSendCorrectMessage () throws ExitClientException, IOException {
        int port = 666;
        String host = "localhost";
        ClientSession mockClientSession = mock(ClientSession.class);
        Client client = new Client(mockClientSession);
        client.setUserName("NAME");
        String testMessage = "/snd this is my test message";
        client.send(testMessage);
        verify(mockClientSession).sendMessage("/snd NAME -> this is my test message");
    }

    @Test
    public void shouldProcessWrongCommandsCorrectly() throws ExitClientException, IOException {
        int errorCodeReturned;
        int port = 666;
        String host = "localhost";
        Client client = new Client (port, host);
        client.setUserName("NAME");
        String testMessage = "/abc123";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-1, errorCodeReturned);
    }

    @Test
    public void shouldNotSendEmptyMessages () throws ExitClientException, IOException {
        int errorCodeReturned;
        int port = 666;
        String host = "localhost";
        Client client = new Client (port, host);
        client.send("NAME");
        String testMessage = "/snd";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-2, errorCodeReturned);
    }

    @Test(expected = ExitClientException.class)
    public void shouldProcessExitCommandCorrectly () throws ExitClientException, IOException {
        int port = 666;
        String host = "localhost";
        Client client = new Client (port, host);
        client.send("NAME");
        String testMessage = "/exit";
        client.send(testMessage);
    }

    @Test
    public void shouldProcessNonCommandConsoleInputCorrectly () throws ExitClientException, IOException {
        int port = 666;
        int errorCodeReturned;
        String host = "localhost";
        Client client = new Client (port, host);
        client.send("NAME");
        String testMessage = "abc123";
        errorCodeReturned = client.send(testMessage);
        assertEquals(-10, errorCodeReturned);
    }

    @Test
    public void shouldNotAllowToSendMessageMoreThan150CharsLong() throws ExitClientException, IOException {
        int port = 666;
        String host = "localhost";
        int errorCodeReturned;
        String testMessage = new String();
        Client client = new Client (port, host);

        for (int i = 0; i <153; i++) {
            testMessage += "a";
        }

        testMessage = "/snd " + testMessage;
        client.send("NAME");
        errorCodeReturned = client.send(testMessage);
        assertEquals(-3, errorCodeReturned);
    }

    @Test
    public void shouldCloseClient() throws IOException {
        BufferedReader br = mock(BufferedReader.class);
        when(br.readLine()).thenReturn("/exit");
        ExecutorService e = mock(ExecutorService.class);
        int port = 666;
        String host = "localhost";
        Client client = new Client (port, host);

        client.sendRunningThread(br, e);
        assertEquals(client.isClosed(), true);
        String s = client.receive();
        assertEquals(s, "");
    }

}

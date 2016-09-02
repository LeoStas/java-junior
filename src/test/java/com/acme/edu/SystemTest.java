package com.acme.edu;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Java_4 on 02.09.2016.
 */
public class SystemTest implements SysoutCaptureAndAssertionAbility {
    private ExecutorService executorService;
    private volatile Server server;

    @Before
    public void setUp() {

        executorService =Executors.newFixedThreadPool(1);
        executorService.execute(()->

        {
            try {
                server = new Server();
                server.runServer();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    @After
    public void clear() {
        server.shutdownServer();
        executorService.shutdownNow();
    }

    @Test
    public void whenMessageIsSentTheOtherClientShouldReceiveIt() throws IOException {

        ExecutorService pool = Executors.newFixedThreadPool(3);
        Client c1 = new Client (1111, "localhost");
        Client c2 = new Client (1111, "localhost");
        String testMessage = "123321";

        BufferedReader mockBufferedReader = mock(BufferedReader.class);
        ExecutorService mockExecutorService = mock(ExecutorService.class);


        when(mockBufferedReader.readLine()).thenReturn("/snd "+testMessage);

        pool.execute(() -> {
            c1.receiveRunningThread(mockExecutorService);
            verify(mockExecutorService).
        });
        pool.execute(() -> c2.receiveRunningThread(mockExecutorService));
        pool.execute(()-> {
            try {
                c1.processAndSendInputString(mockBufferedReader);
            } catch (ExitClientException e) {
                e.printStackTrace();
            }
        });

        //ClientOne.sendRunningThread(mockBufferedReader reader, mockExecutorService executorService);


    }




}

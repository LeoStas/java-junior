package com.acme.edu;

import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * Created by Java_4 on 02.09.2016.
 */
public class SystemTest {

    @Test
    public void whenMessageIsSentTheOtherClientShouldReceiveIt() throws IOException {
        Client ClientOne = new Client (1111, "localhost");
        Client ClientTwo = new Client (1111, "localhost");

        Client mockClient = mock(Client.class);

        //mockClient.





    }


}

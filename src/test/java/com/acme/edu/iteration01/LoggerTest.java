package com.acme.edu.iteration01;

import com.acme.edu.ConsolePrinter;
import com.acme.edu.Logger;
import com.acme.edu.SysoutCaptureAndAssertionAbility;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
public class LoggerTest implements SysoutCaptureAndAssertionAbility {
    //region given
    @Before
    public void setUpSystemOut() throws IOException {
        resetOut();
        captureSysout();
    }

    @After
    public void tearDown() {
        resetOut();
    }
    //endregion

    @Test
    public void shouldLogInteger() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter(),
                new ConsolePrinter() {
                    public void print(String msg) {
                        System.out.print(msg);
                    }
                }
        );
        logger.log(1);
        logger.log(0);
        logger.log(-1);
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("primitive: 0");
        //endregion
    }

    @Test
    public void shouldLogByte() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter());
        logger.log((byte)1);
        logger.log((byte)0);
        logger.log((byte)-1);
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("primitive: 0");
        //endregion
    }

    @Test
    public void shouldLogChar() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter());
        logger.log('a');
        logger.log('b');
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("char: ");
        assertSysoutContains("a");
        assertSysoutContains("b");
        //endregion
    }


    @Test
    public void shouldLogString() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter());
        logger.log("test string 1");
        logger.log("other str");
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("string: ");
        assertSysoutContains("test string 1");
        assertSysoutContains("other str");
        //endregion
    }

    @Test
    public void shouldLogBoolean() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter());
        logger.log(true);
        logger.log(false);
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("primitive: ");
        assertSysoutContains("true");
        assertSysoutContains("false");
        //endregion
    }

    @Test
    public void shouldLogReference() throws IOException {
        //region when
        Logger logger = new Logger(new ConsolePrinter());
        logger.log(new Object());
        logger.closeLogSession();
        //endregion

        //region then
        assertSysoutContains("reference: ");
        assertSysoutContains("@");
        //endregion
    }


}
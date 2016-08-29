package com.acme.edu;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.fest.assertions.Assertions.*;

public interface SysoutCaptureAndAssertionAbility {
    ByteArrayOutputStream OUT = new ByteArrayOutputStream();
    ByteArrayOutputStream ERR = new ByteArrayOutputStream();

    default void captureSysout() {
        System.setOut(new PrintStream(OUT));
    }

    default void captureSyserr() {
        System.setErr(new PrintStream(ERR));
    }

    default void assertSysoutEquals(String expected) {
        assertThat(OUT.toString()).isEqualTo(expected);
    }

    default void assertSyserrEquals(String expected) {
        assertThat(ERR.toString()).isEqualTo(expected);
    }


    default void assertSysoutContains(String expected) {
        assertThat(OUT.toString()).contains(expected);
    }

    default void assertSyserrContains(String expected) {
        assertThat(ERR.toString()).contains(expected);
    }

    default void resetOut() {
        OUT.reset();
    }

    default void resetErr() {
        ERR.reset();
    }
}

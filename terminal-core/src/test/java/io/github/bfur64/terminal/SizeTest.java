package io.github.bfur64.terminal;

import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SizeTest {
    private MockRuntime runtime;
    private Terminal terminal;

    @Before public void setUp() throws Exception {
        runtime = (MockRuntime) Terminal.builder().mock().build();
        terminal = runtime.terminal();
    }

    @After public void tearDown() {
        runtime.close();
    }

    @Test public void defaultSize_isZero() {
        assertEquals(0, terminal.xSize());
        assertEquals(0, terminal.ySize());
    }

    @Test public void newSizeTest() {
        runtime.setXSize(25);
        runtime.setYSize(50);

        assertEquals(25, terminal.xSize());
        assertEquals(50, terminal.ySize());
    }
}

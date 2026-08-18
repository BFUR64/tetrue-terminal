package io.github.bfur64.terminal;

import io.github.bfur64.terminal.commands.Put;
import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrintTest {
    private MockRuntime runtime;
    private Terminal terminal;

    @Before public void setUp() throws Exception {
        runtime = (MockRuntime) Terminal.builder().mock().build();
        terminal = runtime.terminal();
    }

    @After public void tearDown() {
        runtime.close();
    }

    @Test public void newTerminal_hasEmptyBuffer() {
        assertTrue(terminal.snapshotBuffer().isEmpty());
    }

    @Test public void put_textAtPosition_addsToBuffer() {
        terminal.put(0, 0, "Hello World!");

        assertEquals(new Put(0, 0, "Hello World!"), terminal.snapshotBuffer().getFirst());
    }
}

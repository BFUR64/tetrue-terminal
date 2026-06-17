package io.github.bfur64.terminal;

import io.github.bfur64.terminal.commands.Put;
import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrintTest {
    @Test public void noPrintTest() throws Exception {
        try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
            Terminal terminal = runtime.terminal();

            assertTrue(terminal.snapshotBuffer().isEmpty());
        }
    }

    @Test public void simplePrintTest() throws Exception {
        try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
            Terminal terminal = runtime.terminal();

            terminal.put(0, 0, "Hello World!");

            assertEquals(new Put(0, 0, "Hello World!"), terminal.snapshotBuffer().getFirst());
        }
    }
}

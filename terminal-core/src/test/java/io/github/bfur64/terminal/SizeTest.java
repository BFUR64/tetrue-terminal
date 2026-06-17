package io.github.bfur64.terminal;

import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import org.junit.Test;

import static org.junit.Assert.*;

public class SizeTest {
    @Test public void defaultSizeTest() throws Exception {
        try (MockRuntime mockRuntime = (MockRuntime) Terminal.builder().mock().build();) {
            Terminal terminal = mockRuntime.terminal();

            assertEquals(0, terminal.xSize());
            assertEquals(0, terminal.ySize());
        }
    }

    @Test public void newSizeTest() throws Exception {
        try (MockRuntime mockRuntime = (MockRuntime) Terminal.builder().mock().build();) {
            Terminal terminal = mockRuntime.terminal();

            mockRuntime.setXSize(25);
            mockRuntime.setYSize(50);

            assertEquals(25, terminal.xSize());
            assertEquals(50, terminal.ySize());
        }
    }
}

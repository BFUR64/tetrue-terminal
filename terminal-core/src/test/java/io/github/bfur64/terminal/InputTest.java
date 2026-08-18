package io.github.bfur64.terminal;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputTest {
    private MockRuntime runtime;
    private Terminal terminal;

    @Before public void setUp() throws Exception {
        runtime = (MockRuntime) Terminal.builder().mock().build();
        terminal = runtime.terminal();
    }

    @After public void tearDown() {
        runtime.close();
    }

    @Test public void noInput_returnsNull() {
        assertNull(terminal.poll());
    }

    @Test public void addKeyStroke_thenRead_returnsSameKeyStroke() {
        runtime.addKeyStroke(new KeyStroke(KeyType.PAGE_UP));

        assertEquals(new KeyStroke(KeyType.PAGE_UP), terminal.read());
    }
}

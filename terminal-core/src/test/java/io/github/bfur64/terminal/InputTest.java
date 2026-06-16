package io.github.bfur64.terminal;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.mock.MockRuntime;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputTest {
    @Test public void noInputTest() throws Exception {
        try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
            Terminal terminal = runtime.terminal();

            assertNull(terminal.poll());
        }
    }

    @Test public void oneInputTest() throws Exception {
        try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
            Terminal terminal = runtime.terminal();

            runtime.addKeyStroke(new KeyStroke(KeyType.PAGE_UP));

            assertEquals(new KeyStroke(KeyType.PAGE_UP), terminal.read());
        }
    }
}

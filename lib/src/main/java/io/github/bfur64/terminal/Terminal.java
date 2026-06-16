package io.github.bfur64.terminal;

import io.github.bfur64.terminal.impl.jline.JLineBackend;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import io.github.bfur64.terminal.impl.lanterna.LanternaBackend;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public class Terminal {

    private Terminal() {
    }

    public static TerminalBackend auto() throws IOException {
        if (isTermux()) {
            return new LanternaBackend();
        }
        else {
            return new JLineBackend();
        }
    }

    public static TerminalBackend lanterna() throws IOException {
        return new LanternaBackend();
    }

    public static TerminalBackend jline() throws IOException {
        return new JLineBackend();
    }

    private static boolean isTermux() {
        String prefix = System.getenv("PREFIX");

        return (prefix != null &&
            prefix.contains("termux")) ||
            System.getenv("TERMUX_VERSION") != null;
    }
}

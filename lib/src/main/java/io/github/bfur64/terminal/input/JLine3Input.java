package io.github.bfur64.terminal.input;

import org.jline.terminal.Terminal;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

public class JLine3Input implements TerminalInput {
    private final Terminal terminal;

    public JLine3Input(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public @NonNull KeyStroke readInput() {
        try {
            int first = terminal.reader().read();

            if (first == 127 || first == 8) { // Backspace (DEL or BS)
                return new KeyStroke(KeyType.BACKSPACE);
            }

            if (first == 13 || first == 10) { // Enter (CR or LF)
                return new KeyStroke(KeyType.ENTER);
            }

            if (first == 27) { // ESC
                int second = terminal.reader().read(10);

                if (second == -2) {
                    return new KeyStroke(KeyType.ESCAPE);
                }

                if (second == '[' || second == 'O') {
                    int third = terminal.reader().read(10);


                    //noinspection StatementWithEmptyBody
                    while (terminal.reader().read(10) >= 0) {} // Drain the buffer for longer sequences

                    return switch (third) {
                        case 'A' -> new KeyStroke(KeyType.ARROW_UP);
                        case 'B' -> new KeyStroke(KeyType.ARROW_DOWN);
                        case 'C' -> new KeyStroke(KeyType.ARROW_RIGHT);
                        case 'D' -> new KeyStroke(KeyType.ARROW_LEFT);
                        default -> new KeyStroke(KeyType.UNKNOWN);
                    };
                }

                //noinspection StatementWithEmptyBody
                while (terminal.reader().read(10) >= 0) {} // Drain the buffer for longer sequences

                return new KeyStroke(KeyType.UNKNOWN);
            }

            return new KeyStroke((char) first);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        try {
            if (terminal.reader().ready()) {
                return readInput();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}

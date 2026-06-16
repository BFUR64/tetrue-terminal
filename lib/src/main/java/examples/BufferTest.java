package examples;

import io.github.bfur64.terminal.*;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

import java.io.IOException;

public class BufferTest {

    public static void main(String[] args) throws IOException {
        final TerminalBackend terminal = Terminal.auto();
        terminal.start();

        int x = 0;
        int y = 0;

        final SymbolBuffer buffer = new SymbolBuffer(terminal.getSize());
        terminal.clearScreen();

        while (true) {
            if (x >= buffer.getWidth()) {
                x = 0;
                y++;
            }

            if (y >= buffer.getHeight()) {
                y = 0;
                terminal.flushBuffer(buffer);
            }

            buffer.set(x, y, new Symbol('1', Colors.GREEN, Colors.BLUE));

            x++;
        }
    }
}

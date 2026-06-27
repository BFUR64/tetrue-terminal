package examples;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.commands.Command;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

import java.util.ArrayList;
import java.util.List;

public final class BufferShowcase {
    public static void main(String[] args) throws Exception {
        List<Command> buffer;

        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            terminal.clear();

            KeyStroke keyStroke = new KeyStroke(KeyType.UNKNOWN);

            terminal.put(0, 0, "Current Renderer: " + terminal.terminalInfo());
            terminal.put(0, 1, "Cols: " + terminal.xSize() + " | " + "Rows: " + terminal.ySize());

            terminal.put(0, 3, "Character: " + keyStroke);
            terminal.put(0, 5, "Press any key to exit (The buffer will be printed)");

            // The snapshot needs to be here as `flush()` will clear the internal buffer
            buffer = new ArrayList<>(terminal.snapshotBuffer());

            terminal.flush();

            terminal.read();
        }

        System.out.println(buffer);
    }
}

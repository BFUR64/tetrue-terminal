package examples;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.commands.Command;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        List<Command> buffer = new ArrayList<>();

        try (TerminalRuntime runtime = Terminal.builder().auto().buffered().build()) {
            Terminal terminal = runtime.terminal();

            KeyStroke keyStroke = new KeyStroke('t');

            do {
                terminal.clear();
                terminal.put(0, 0, "Current Renderer: " + terminal.terminalInfo());
                terminal.put(0, 1, "Cols: " + terminal.xSize() + " | " + "Rows: " + terminal.ySize());

                terminal.put(0, 3, "Character: " + keyStroke);
                buffer.addAll(terminal.snapshotBuffer());
                terminal.flush();

                if (keyStroke.keyType() == KeyType.ESCAPE) {
                    break;
                }

                keyStroke = terminal.read();
            }
            while (true);
        }

        System.out.println(buffer);
    }
}

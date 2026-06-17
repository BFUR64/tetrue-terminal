package examples;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.commands.Command;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.output.SGR;

import java.util.ArrayList;
import java.util.List;

public final class Test {
    public static void main(String[] args) throws Exception {
        List<Command> buffer = new ArrayList<>();

        try (TerminalRuntime runtime = Terminal.builder().auto().buffered().build()) {
            Terminal terminal = runtime.terminal();

            KeyStroke keyStroke = new KeyStroke('t');

            do {
                terminal.clear();
                terminal.onSGR(SGR.UNDERLINE, SGR.BOLD, SGR.ITALIC);
                terminal.put(0, 0, "Current Renderer: " + terminal.terminalInfo());
                terminal.put(0, 1, "Cols: " + terminal.xSize() + " | " + "Rows: " + terminal.ySize());
                terminal.offSGR(SGR.UNDERLINE, SGR.BOLD, SGR.ITALIC);

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

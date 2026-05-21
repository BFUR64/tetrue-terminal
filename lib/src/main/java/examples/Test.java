package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

public class Test {
    public static void main(String[] args) throws Exception {
        try (TerminalBackend terminal = Terminal.auto()) {
            terminal.start();

            KeyStroke keyStroke = new KeyStroke('t');

            do {
                terminal.clearScreen();
                terminal.put(0, 0, "Current Renderer: " + terminal.getTerminalInfo());

                terminal.put(0, 2, "Character: " + keyStroke);
                terminal.flush();

                if (keyStroke.keyType() == KeyType.ESCAPE) {
                    break;
                }

                keyStroke = terminal.readInput();
            }
            while (true);
        }
    }
}

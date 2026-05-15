package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        try (
                Terminal terminal = Terminal.jline3();
        ) {
            KeyStroke keyStroke = new KeyStroke('t');

            do {
                String toPrint;

                if (keyStroke.getCharacter() != null) {
                    toPrint = "" + keyStroke.getCharacter();
                } else {
                    toPrint = switch (keyStroke.getKeyType()) {
                        case CHARACTER -> null;
                        case ESCAPE -> "Escape";
                        case BACKSPACE -> "Backspace";
                        case ENTER -> "Enter";
                        case ARROW_UP -> "Arrow Up";
                        case ARROW_DOWN -> "Arrow Down";
                        case ARROW_LEFT -> "Arrow Left";
                        case ARROW_RIGHT -> "Arrow Right";
                        case UNKNOWN -> "Unknown";
                    };
                }

                terminal.clearScreen();
                terminal.putString(0, 0, "Renderer: " + terminal.getTerminalInfo());
                terminal.putString(0, 1, "Character: " + toPrint);
                terminal.flush();

                if (keyStroke.getKeyType() == KeyType.ESCAPE) {
                    break;
                }

                keyStroke = terminal.readInput();
            }
            while (true);
        }
    }
}

package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        try (
                Terminal terminal = Terminal.jline3();
        ) {
            KeyStroke keyStroke = new KeyStroke('t');

            do {
                String toPrint = getCharacterString(keyStroke);

                terminal.clearScreen();
                terminal.putString(0, 0, "Current Renderer: " + terminal.getCurrentTerminal());

                List<String> terminalInfo = terminal.getTerminalInfo();

                for (int i = 0; i < terminalInfo.size(); i++) {
                    terminal.putString(0, 1 + i, terminalInfo.get(i));
                }

                terminal.putString(0, terminalInfo.size() + 2, "Character: " + toPrint);
                terminal.flush();

                if (keyStroke.getKeyType() == KeyType.ESCAPE) {
                    break;
                }

                keyStroke = terminal.readInput();
            }
            while (true);
        }
    }

    private static @Nullable String getCharacterString(KeyStroke keyStroke) {
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
                case HOME -> "Home";
                case END -> "End";
                case UNKNOWN -> "Unknown";
            };
        }
        return toPrint;
    }
}

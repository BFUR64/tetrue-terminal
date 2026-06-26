package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.output.TextColor;

public class FeatureShowcase {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            loop:
            while (true) {
                terminal.clear();

                terminal.put(2, 0, "<< Feature Showcase >>");
                terminal.put(0, 2, "1. Static Showcase");
                terminal.put(0, 3, "2. Dynamic Showcase");
                terminal.put(0, 4, "0. Exit");

                terminal.flush();

                KeyStroke keyStroke = terminal.read();

                if (keyStroke.keyType() == KeyType.ESCAPE) break;

                if (keyStroke.keyType() == KeyType.CHARACTER && keyStroke.character() != null) {
                    switch (keyStroke.character()) {
                        case '1' -> staticShowcase(terminal);
                        case '2' -> {}
                        case '0' -> { break loop; }
                    }
                }
            }
        }
    }

    private static void staticShowcase(Terminal terminal) {
        terminal.clear();

        terminal.setBg(TextColor.TOMATO);
        terminal.setFg(TextColor.BEIGE);
        terminal.put(1, 1, " Hello World! ");
        terminal.reset();

        terminal.put(1, 3, 'a');
        terminal.onSGR(SGR.BOLD);
        terminal.put(2, 3, 'a');
        terminal.reset();

        terminal.onSGR(SGR.UNDERLINE);
        terminal.put(3, 3, 'a');
        terminal.reset();

        terminal.onSGR(SGR.ITALIC);
        terminal.put(4, 3, 'a');
        terminal.reset();

        terminal.onSGR(SGR.STRIKETHROUGH);
        terminal.put(5, 3, 'a');
        terminal.reset();

        terminal.onSGR(SGR.REVERSE);
        terminal.put(6, 3, 'a');
        terminal.reset();

        terminal.onSGR(SGR.UNDERLINE);
        terminal.put(3, 5, "I am ");
        terminal.onSGR(SGR.BOLD);
        terminal.put(8, 5, "UNDERLINED");
        terminal.reset();

        terminal.flush();

        terminal.read();
    }
}

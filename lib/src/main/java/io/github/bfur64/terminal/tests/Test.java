package io.github.bfur64.terminal.tests;

import io.github.bfur64.terminal.input.JLine3Input;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.render.JLine3Renderer;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        JLine3Renderer renderer = new JLine3Renderer();
        JLine3Input input = new JLine3Input(renderer.getTerminal());

        KeyStroke keyStroke = new KeyStroke('t');

        do {
            String toPrint;

            if (keyStroke.getCharacter() != null) {
                toPrint = "" + keyStroke.getCharacter();
            }
            else {
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

            renderer.clearScreen();
            renderer.putString(0, 0, "Character: " + toPrint);
            renderer.flush();

            if (keyStroke.getKeyType() == KeyType.ESCAPE) {
                break;
            }

            keyStroke = input.readInput();
        }
        while (true);

        renderer.close();
    }
}

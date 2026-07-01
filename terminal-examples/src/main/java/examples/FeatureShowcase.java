package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.output.Style;
import io.github.bfur64.terminal.output.TextColor;

import java.awt.*;

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
                        case '2' -> dynamicShowcase(terminal);
                        case '0' -> { break loop; }
                    }
                }
            }
        }
    }

    private static void staticShowcase(Terminal terminal) {
        terminal.clear();

        terminal.put(1, 1, " Hello World! ", Style.DEFAULT.bg(TextColor.TOMATO).fg(TextColor.BEIGE));

        terminal.put(1, 3, 'a');
        terminal.put(2, 3, 'a', Style.DEFAULT.bold());
        terminal.put(3, 3, 'a', Style.DEFAULT.underline());
        terminal.put(4, 3, 'a', Style.DEFAULT.italic());
        terminal.put(5, 3, 'a', Style.DEFAULT.strikethrough());
        terminal.put(6, 3, 'a', Style.DEFAULT.reverse());
        terminal.put(3, 5, "I am ", Style.DEFAULT.underline());
        terminal.put(8, 5, "UNDERLINED", Style.DEFAULT.bold().underline());

        terminal.flush();

        terminal.read();
    }

    private static void dynamicShowcase(Terminal terminal) throws InterruptedException {
        terminal.clear();

        int offset = 0; // Scrolling offset for the bar
        int flashingCounter = 0;
        boolean flash = true;

        while (true) {
            KeyStroke keyStroke = terminal.poll();
            if (keyStroke != null && keyStroke.keyType() == KeyType.ESCAPE) break;

            drawBar(terminal, 0, 0, offset);
            drawFlashingText(terminal, 0, 2, flash, "Hello World!");
            terminal.reset();
            drawFlashingText(terminal, 5, 4, flash, "TETRUE TERMINAL");

            if (flashingCounter > 100) {
                flashingCounter = 0;
                flash = !flash;
            }

            terminal.flush();

            Thread.sleep(1); // Animation speed

            offset = (offset + 3) % 360;  // Move the bar gradient leftward
            flashingCounter++;
        }

        terminal.reset();
    }

    private static void drawBar(Terminal terminal, int xPos, int yPos, int offset) {
        int barXSize = 30;

        // Draw the whole horizontal bar with a moving gradient
        for (int x = xPos; x <= barXSize; x++) {
            // Calculate hue based on X position and scrolling offset
            float hue = (x * 15 + offset) % 360; // 15 degree step between columns
            int[] rgb = hsbToRgb(hue / 360f);

            terminal.setFg(rgb[0], rgb[1], rgb[2]);
            terminal.put(x, yPos, '█');
        }
    }

    private static void drawFlashingText(Terminal terminal, int x, int y, boolean flash, String out) {
        if (flash) {
            terminal.onSGR(SGR.REVERSE);
            terminal.put(x, y, out);
            terminal.reset();
        }
        else {
            terminal.put(x, y, out);
        }
    }

    private static int[] hsbToRgb(float hue) {
        int rgb = Color.HSBtoRGB(hue, (float) 1.0, (float) 1.0);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new int[]{r, g, b};
    }
}

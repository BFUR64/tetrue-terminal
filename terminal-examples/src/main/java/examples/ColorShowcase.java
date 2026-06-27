package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

public final class ColorShowcase {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();
            
            int w = terminal.xSize();
            int h = terminal.ySize();

            terminal.clear();
            terminal.put(0, 0, "Color Showcase (Full Screen Alternating Colours)");
            terminal.put(0, 1, "Terminal resizing will break the test");
            terminal.put(0, 2, "Press [ESC] to escape, [ENTER] to skip the countdown");
            runCountDown(terminal, 3, 2, 4);

            terminal.clear();
            terminal.put(0, 0, "Alternating dark gray and gray test");
            runCountDown(terminal, 3, 2, 2);

            runStress1(terminal, h, w);
            runCountDown(terminal, 1, 0, 0);

            terminal.clear();
            terminal.flush();

            runStress1(terminal, h, w);
            runCountDown(terminal, 1, 0, 0);

            terminal.clear();
            terminal.put(0, 0, "RGB Hue Test");
            terminal.put(0, 1, "There should be leftover colors present on these texts");
            runCountDown(terminal, 3, 2, 3);

            runStress2(terminal, h, w, 'r');
            runCountDown(terminal, 1, 0, 0);

            runStress2(terminal, h, w, 'g');
            runCountDown(terminal, 1, 0, 0);

            runStress2(terminal, h, w, 'b');
            runCountDown(terminal, 1, 0, 0);

            terminal.reset();
            terminal.clear();
            terminal.put(0, 0, "RGB Rainbow Test");
            terminal.put(0, 1, "There should be no leftover colors on these texts");
            terminal.put(0, 2, "The screen may flicker!");
            runCountDown(terminal, 4, 2, 4);

            for (int i = 0; i < 3; i++ ) {
                runStress3(terminal, h, w);
                runCountDown(terminal, 1, 0, 0);
            }

            terminal.clear();
            terminal.put(0, 0, "Alternating color test");
            terminal.put(0, 1, "A lot of color changes will happen!");
            runCountDown(terminal, 4, 2, 3);

            runStress4(terminal, h, w, 255, 0, 0);     // Red
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 0, 255, 0);     // Green
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 0, 0, 255);     // Blue
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 255, 255, 0);   // Yellow
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 0, 255, 255);   // Cyan
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 255, 0, 255);   // Magenta
            runCountDown(terminal, 1, 0, 0);

            runStress4(terminal, h, w, 255, 255, 255); // White
            runCountDown(terminal, 1, 0, 0);

            terminal.reset();
            terminal.clear();
            terminal.put(0, 0, "All tests will run without delay");
            terminal.put(0, 1, "The screen will change a lot!!!");
            runCountDown(terminal, 5, 2, 3);

            runStress1(terminal, h, w);

            terminal.clear();
            terminal.flush();

            runStress1(terminal, h, w);

            terminal.clear();
            terminal.flush();

            runStress1(terminal, h, w);

            for (int i = 0; i < 10; i++) {
                runStress2(terminal, h, w, 'r');
                runStress2(terminal, h, w, 'g');
                runStress2(terminal, h, w, 'b');

                runStress3(terminal, h, w);
                runStress3(terminal, h, w);
                runStress3(terminal, h, w);

                runStress4(terminal, h, w, 255, 0, 0);     // Red
                runStress4(terminal, h, w, 0, 255, 0);     // Green
                runStress4(terminal, h, w, 0, 0, 255);     // Blue
                runStress4(terminal, h, w, 255, 255, 0);   // Yellow
                runStress4(terminal, h, w, 0, 255, 255);   // Cyan
                runStress4(terminal, h, w, 255, 0, 255);   // Magenta
                runStress4(terminal, h, w, 255, 255, 255); // White
            }

            terminal.reset();
            terminal.clear();
            terminal.put(0, 0, "All tests are done");
            runCountDown(terminal, 3, 2, 2);
        }
    }

    private static void runStress1(Terminal terminal, int h, int w) {
        terminal.reset();
        terminal.clear();

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                boolean even = ((row + col) & 1) == 0;
                terminal.setBg(even ? 30 : 60, even ? 30 : 60, even ? 30 : 60);
                terminal.put(col, row, ".");
            }
        }

        terminal.flush();
    }

    private static void runStress2(Terminal terminal, int h, int w, char colorChannel) {
        terminal.reset();
        terminal.clear();

        // Create a string of spaces for the full width
        String fullRow = " ".repeat(Math.max(0, w));

        for (int y = 0; y < h; y++) {
            int colorValue = y * 255 / (h - 1);

            // Set color based on the selected channel
            switch (colorChannel) {
                case 'r':
                case 'R':
                    terminal.setBg(colorValue, 0, 0);
                    break;
                case 'g':
                case 'G':
                    terminal.setBg(0, colorValue, 0);
                    break;
                case 'b':
                case 'B':
                    terminal.setBg(0, 0, colorValue);
                    break;
                default:
                    terminal.setBg(colorValue, 0, 0); // Default to red
                    break;
            }

            terminal.put(0, y, fullRow);
        }

        terminal.flush();
    }

    private static void runStress3(Terminal terminal, int h, int w) {
        terminal.reset();
        terminal.clear();
        terminal.flush();

        // Create a string of dots for the full width
        String fullRow = " ".repeat(Math.max(0, w));

        for (int y = 0; y < h; y++) {
            // Use HSV-like color cycling
            float hue = (float) y / h;
            int[] rgb = hsvToRgb(hue);

            terminal.setBg(rgb[0], rgb[1], rgb[2]);
            terminal.put(0, y, fullRow);
        }

        terminal.flush();
    }

    private static void runStress4(Terminal terminal, int h, int w, int r, int g, int b) {
        terminal.reset();
        terminal.clear();

        String fullRow = ".".repeat(Math.max(0, w));

        terminal.setBg(r, g, b);

        for (int y = 0; y < h; y++) {
            terminal.put(0, y, fullRow);
        }

        terminal.flush();
    }

    private static void runCountDown(Terminal terminal, int seconds, int x, int y) throws InterruptedException {
        while (seconds > 0) {
            KeyStroke keyStroke = terminal.poll();

            if (keyStroke != null) {
                if (keyStroke.keyType() == KeyType.ESCAPE) {
                    System.exit(0);
                }

                if (keyStroke.keyType() == KeyType.ENTER) {
                    return;
                }
            }

            terminal.put(x, y, "Running in " + seconds + "...");
            terminal.flush();

            Thread.sleep(1000);
            seconds--;
        }
    }

    private static int[] hsvToRgb(float h) {
        float r = 0, g = 0, b = 0;

        int i = (int) (h * 6);
        float t = h * 6 - i;
        float p = 0.0f;
        float q = 1 - t;

        switch (i % 6) {
            case 0: r = (float) 1.0; g = t; b = p; break;
            case 1: r = q; g = (float) 1.0; b = p; break;
            case 2: r = p; g = (float) 1.0; b = t; break;
            case 3: r = p; g = q; b = (float) 1.0; break;
            case 4: r = t; g = p; b = (float) 1.0; break;
            case 5: r = (float) 1.0; g = p; b = q; break;
        }

        return new int[] {
            Math.round(r * 255),
            Math.round(g * 255),
            Math.round(b * 255)
        };
    }
}

package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

public final class ExtremeTest {
    public static void main(String[] args) {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();
            
            int w = terminal.xSize();
            int h = terminal.ySize();

            terminal.reset();
            terminal.clear();
            terminal.put(0, 0, "Next test: Stress Test (Full Screen Alternating Colours)");
            terminal.flush();

            Thread.sleep(3000);

            for (int i = 0; i < 1; i++) {
                runStress1(terminal, h, w);
                Thread.sleep(250);
            }

            for (int i = 0; i < 1; i++) {
                runStress2(terminal, h, w, 'r');
                Thread.sleep(250);
                runStress2(terminal, h, w, 'g');
                Thread.sleep(250);
                runStress2(terminal, h, w, 'b');
                Thread.sleep(250);
            }

            for (int i = 0; i < 3; i++ ) {
                runStress3(terminal, h, w);
                Thread.sleep(250);
            }

            for (int i = 0; i < 2; i++) {
                runStress4(terminal, w, h, 255, 0, 0);    // Red
                Thread.sleep(250);
                runStress4(terminal, w, h, 0, 255, 0);    // Green
                Thread.sleep(250);
                runStress4(terminal, w, h, 0, 0, 255);    // Blue
                Thread.sleep(250);
                runStress4(terminal, w, h, 255, 255, 0);  // Yellow
                Thread.sleep(250);
                runStress4(terminal, w, h, 0, 255, 255);  // Cyan
                Thread.sleep(250);
                runStress4(terminal, w, h, 255, 0, 255);  // Magenta
                Thread.sleep(250);
                runStress4(terminal, w, h, 255, 255, 255); // White
                Thread.sleep(250);
            }

            terminal.reset();
            terminal.clear();
            terminal.put(0, 0, "lol");
            terminal.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runStress1(Terminal terminal, int h, int w) {
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
    public static void runStress2(Terminal terminal, int h, int w, char colorChannel) {
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

            // Write the entire row at once
            terminal.put(0, y, fullRow);
        }

        terminal.flush();
    }

    public static void runStress3(Terminal renderer, int h, int w) {
        renderer.clear();

        // Create a string of dots for the full width
        String fullRow = " ".repeat(Math.max(0, w));

        for (int y = 0; y < h; y++) {
            // Use HSV-like color cycling - hue shifts vertically
            float hue = (float) y / h;
            int[] rgb = hsvToRgb(hue, 1.0f, 1.0f);

            renderer.setBg(rgb[0], rgb[1], rgb[2]);
            renderer.put(0, y, fullRow);
        }

        renderer.flush();
    }

    public static void runStress4(Terminal renderer, int w, int h, int r, int g, int b) {
        renderer.clear();

        String fullRow = ".".repeat(Math.max(0, w));

        renderer.setBg(r, g, b);

        for (int y = 0; y < h; y++) {
            renderer.put(0, y, fullRow);
        }

        renderer.flush();
    }

    // Helper method to convert HSV to RGB
    private static int[] hsvToRgb(float h, float s, float v) {
        float r = 0, g = 0, b = 0;

        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            case 5: r = v; g = p; b = q; break;
        }

        return new int[] {
                Math.round(r * 255),
                Math.round(g * 255),
                Math.round(b * 255)
        };
    }
}

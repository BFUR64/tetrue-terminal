package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

import java.io.IOException;

public class ExtremeTest {
    public static void main(String[] args) {
        try (TerminalBackend renderer = Terminal.auto()) {
            renderer.start();

            int w = renderer.getWidth();
            int h = renderer.getHeight();

            renderer.resetColorAndStyle();
            renderer.clearScreen();
            renderer.put(0, 0, "Next test: Stress Test (Full Screen Alternating Colours)");
            renderer.flush();

            Thread.sleep(3000);

            for (int i = 0; i < 1; i++) {
                runStress1(renderer, h, w);
                Thread.sleep(250);
            }

            for (int i = 0; i < 1; i++) {
                runStress2(renderer, h, w, 'r');
                Thread.sleep(250);
                runStress2(renderer, h, w, 'g');
                Thread.sleep(250);
                runStress2(renderer, h, w, 'b');
                Thread.sleep(250);
            }

            for (int i = 0; i < 3; i++ ) {
                runStress3(renderer, h, w);
                Thread.sleep(250);
            }

            for (int i = 0; i < 2; i++) {
                runStress4(renderer, w, h, 255, 0, 0);    // Red
                Thread.sleep(250);
                runStress4(renderer, w, h, 0, 255, 0);    // Green
                Thread.sleep(250);
                runStress4(renderer, w, h, 0, 0, 255);    // Blue
                Thread.sleep(250);
                runStress4(renderer, w, h, 255, 255, 0);  // Yellow
                Thread.sleep(250);
                runStress4(renderer, w, h, 0, 255, 255);  // Cyan
                Thread.sleep(250);
                runStress4(renderer, w, h, 255, 0, 255);  // Magenta
                Thread.sleep(250);
                runStress4(renderer, w, h, 255, 255, 255); // White
                Thread.sleep(250);
            }

            renderer.resetColorAndStyle();
            renderer.clearScreen();
            renderer.put(0, 0, "lol");
            renderer.flush();
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runStress1(TerminalBackend renderer, int h, int w) {
        renderer.clearScreen();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                boolean even = ((row + col) & 1) == 0;
                renderer.setBackgroundColor(even ? 30 : 60, even ? 30 : 60, even ? 30 : 60);
                renderer.put(col, row, ".");
            }
        }
        renderer.flush();
    }
    public static void runStress2(TerminalBackend renderer, int h, int w, char colorChannel) {
        renderer.clearScreen();

        // Create a string of spaces for the full width
        String fullRow = " ".repeat(Math.max(0, w));

        for (int y = 0; y < h; y++) {
            int colorValue = y * 255 / (h - 1);

            // Set color based on the selected channel
            switch (colorChannel) {
                case 'r':
                case 'R':
                    renderer.setBackgroundColor(colorValue, 0, 0);
                    break;
                case 'g':
                case 'G':
                    renderer.setBackgroundColor(0, colorValue, 0);
                    break;
                case 'b':
                case 'B':
                    renderer.setBackgroundColor(0, 0, colorValue);
                    break;
                default:
                    renderer.setBackgroundColor(colorValue, 0, 0); // Default to red
                    break;
            }

            // Write the entire row at once
            renderer.put(0, y, fullRow);
        }

        renderer.flush();
    }

    public static void runStress3(TerminalBackend renderer, int h, int w) {
        renderer.clearScreen();

        // Create a string of dots for the full width
        String fullRow = " ".repeat(Math.max(0, w));

        for (int y = 0; y < h; y++) {
            // Use HSV-like color cycling - hue shifts vertically
            float hue = (float) y / h;
            int[] rgb = hsvToRgb(hue, 1.0f, 1.0f);

            renderer.setBackgroundColor(rgb[0], rgb[1], rgb[2]);
            renderer.put(0, y, fullRow);
        }

        renderer.flush();
    }

    public static void runStress4(TerminalBackend renderer, int w, int h, int r, int g, int b) {
        renderer.clearScreen();

        String fullRow = ".".repeat(Math.max(0, w));

        renderer.setBackgroundColor(r, g, b);

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

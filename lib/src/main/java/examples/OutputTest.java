package examples;

import io.github.bfur64.terminal.BufferedTerminal;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

public class OutputTest {
    public static void main(String[] args) throws Exception {
        try (TerminalBackend terminal = BufferedTerminal.auto()) {
            terminal.start();
            runTerminalDebugTest(terminal);
        }
    }

    public static void runTerminalDebugTest(TerminalBackend renderer) throws InterruptedException {
        // 1. Query terminal size (tests getXSize / getYSize)
        int w = renderer.getXSize();
        int h = renderer.getYSize();
        String terminalInfo = renderer.getTerminalInfo();

        // 2. Clear and show basic metadata
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        String info = String.format("Terminal size: %d x %d   True-color test (R,G,B full range)   %s", w, h, terminalInfo);
        renderer.put(0, 0, info);
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: RGB Color Channel Bars (Vertical Gradient)");
        renderer.flush();
        Thread.sleep(3000);

        w = renderer.getXSize();
        h = renderer.getYSize();

        // 3. Test full 0‑255 range for each colour channel as vertical gradient bars
        int barWidth = Math.min(w / 3, 20);
        int startX = 1, startY = 1;
        int maxY = Math.min(h - 2, 256);

        // R bar
        for (int y = 0; y < maxY; y++) {
            int r = y * 255 / (maxY - 1);
            renderer.setBackgroundColor(r, 0, 0);
            for (int x = 0; x < barWidth; x++) {
                renderer.put(startX + x, startY + y, " ");
            }
        }
        // G bar
        int gBarX = startX + barWidth + 1;
        for (int y = 0; y < maxY; y++) {
            int g = y * 255 / (maxY - 1);
            renderer.setBackgroundColor(0, g, 0);
            for (int x = 0; x < barWidth; x++) {
                renderer.put(gBarX + x, startY + y, " ");
            }
        }
        // B bar
        int bBarX = gBarX + barWidth + 1;
        for (int y = 0; y < maxY; y++) {
            int b = y * 255 / (maxY - 1);
            renderer.setBackgroundColor(0, 0, b);
            for (int x = 0; x < barWidth; x++) {
                renderer.put(bBarX + x, startY + y, " ");
            }
        }
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: Foreground Color Spectrum (Horizontal Text)");
        renderer.flush();
        Thread.sleep(3000);

        w = renderer.getXSize();
        h = renderer.getYSize();

        // 4. Test setForegroundColor with text across full RGB spectrum (horizontal strip)
        renderer.resetColorAndStyle();
        int textY = startY + maxY;
        if (textY < h) {
            String sample = "True Color Foreground Test";
            int msgLen = sample.length();
            int maxStepX = Math.min(w - msgLen, 256 * 3 - msgLen);
            for (int step = 0; step < maxStepX; step++) {
                int r = (step * 5) % 256;
                int g = (step * 3 + 85) % 256;
                int b = (step * 7 + 170) % 256;
                renderer.setForegroundColor(r, g, b);
                renderer.setBackgroundColor(20, 20, 20);
                renderer.put(step, textY, sample.substring(0, 1));
            }
        }
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: Reset Color and Style");
        renderer.flush();
        Thread.sleep(3000);

        // 5. Test resetColorAndStyle after coloured output
        renderer.resetColorAndStyle();
        int resetY = textY + 2;
        if (resetY < h) {
            renderer.put(0, resetY, "Colours reset. Text should be default foreground/background.");
        }
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: Edge Cases (Top‑Left and Bottom‑Right Corners)");
        renderer.flush();
        Thread.sleep(3000);

        w = renderer.getXSize();
        h = renderer.getYSize();

        // 6. Edge‑case putString: top‑left and bottom‑right corners
        renderer.setForegroundColor(255, 255, 0);
        renderer.setBackgroundColor(128, 0, 128);
        String cornerTL = "TL";
        String cornerBR = "BR";
        if (w >= 2 && h >= 2) {
            renderer.put(0, 0, cornerTL);
            renderer.put(w - cornerBR.length(), h - 1, cornerBR);
        }
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: Stress Test (Full Screen Alternating Colours)");
        renderer.flush();
        Thread.sleep(3000);

        w = renderer.getXSize();
        h = renderer.getYSize();

        // 7. Stress test: fill entire screen with a character using alternating colours
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                boolean even = ((row + col) & 1) == 0;
                renderer.setBackgroundColor(even ? 30 : 60, even ? 30 : 60, even ? 30 : 60);
                renderer.put(col, row, ".");
            }
        }
        renderer.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping (final message)
        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.put(0, 0, "Next test: Final clear and goodbye message");
        renderer.flush();
        Thread.sleep(3000);

        // 8. Clear screen again, print goodbye message with full intensity white
        renderer.setBackgroundColor(255, 255, 255);
        renderer.setForegroundColor(0, 0, 0);
        renderer.clearScreen();
        renderer.put(0, 0, "All tests completed.");
        renderer.flush();

        Thread.sleep(3000);

        renderer.resetColorAndStyle();
        renderer.clearScreen();
        renderer.flush();
    }
}
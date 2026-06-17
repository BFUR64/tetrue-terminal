package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

public final class OutputTest {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().buffered().build()) {
            Terminal terminal = runtime.terminal();
            runTerminalDebugTest(terminal);
        }
    }

    public static void runTerminalDebugTest(Terminal terminal) throws InterruptedException {
        // 1. Query terminal size (tests getXSize / getYSize)
        int w = terminal.xSize();
        int h = terminal.ySize();
        String terminalInfo = terminal.terminalInfo();

        // 2. Clear and show basic metadata
        terminal.reset();
        terminal.clear();
        String info = String.format("Terminal size: %d x %d   True-color test (R,G,B full range)   %s", w, h, terminalInfo);
        terminal.put(0, 0, info);
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: RGB Color Channel Bars (Vertical Gradient)");
        terminal.flush();
        Thread.sleep(3000);

        w = terminal.xSize();
        h = terminal.ySize();

        // 3. Test full 0‑255 range for each colour channel as vertical gradient bars
        int barWidth = Math.min(w / 3, 20);
        int startX = 1, startY = 1;
        int maxY = Math.min(h - 2, 256);

        // R bar
        for (int y = 0; y < maxY; y++) {
            int r = y * 255 / (maxY - 1);
            terminal.setBg(r, 0, 0);
            for (int x = 0; x < barWidth; x++) {
                terminal.put(startX + x, startY + y, " ");
            }
        }
        // G bar
        int gBarX = startX + barWidth + 1;
        for (int y = 0; y < maxY; y++) {
            int g = y * 255 / (maxY - 1);
            terminal.setBg(0, g, 0);
            for (int x = 0; x < barWidth; x++) {
                terminal.put(gBarX + x, startY + y, " ");
            }
        }
        // B bar
        int bBarX = gBarX + barWidth + 1;
        for (int y = 0; y < maxY; y++) {
            int b = y * 255 / (maxY - 1);
            terminal.setBg(0, 0, b);
            for (int x = 0; x < barWidth; x++) {
                terminal.put(bBarX + x, startY + y, " ");
            }
        }
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: Foreground Color Spectrum (Horizontal Text)");
        terminal.flush();
        Thread.sleep(3000);

        w = terminal.xSize();
        h = terminal.ySize();

        // 4. Test setFg with text across full RGB spectrum (horizontal strip)
        terminal.reset();
        int textY = startY + maxY;
        if (textY < h) {
            String sample = "True Color Foreground Test";
            int msgLen = sample.length();
            int maxStepX = Math.min(w - msgLen, 256 * 3 - msgLen);
            for (int step = 0; step < maxStepX; step++) {
                int r = (step * 5) % 256;
                int g = (step * 3 + 85) % 256;
                int b = (step * 7 + 170) % 256;
                terminal.setFg(r, g, b);
                terminal.setBg(20, 20, 20);
                terminal.put(step, textY, sample.substring(0, 1));
            }
        }
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: Reset Color and Style");
        terminal.flush();
        Thread.sleep(3000);

        // 5. Test reset after coloured output
        terminal.reset();
        int resetY = textY + 2;
        if (resetY < h) {
            terminal.put(0, resetY, "Colours reset. Text should be default foreground/background.");
        }
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: Edge Cases (Top‑Left and Bottom‑Right Corners)");
        terminal.flush();
        Thread.sleep(3000);

        w = terminal.xSize();
        h = terminal.ySize();

        // 6. Edge‑case putString: top‑left and bottom‑right corners
        terminal.setFg(255, 255, 0);
        terminal.setBg(128, 0, 128);
        String cornerTL = "TL";
        String cornerBR = "BR";
        if (w >= 2 && h >= 2) {
            terminal.put(0, 0, cornerTL);
            terminal.put(w - cornerBR.length(), h - 1, cornerBR);
        }
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: Stress Test (Full Screen Alternating Colours)");
        terminal.flush();
        Thread.sleep(3000);

        w = terminal.xSize();
        h = terminal.ySize();

        // 7. Stress test: fill entire screen with a character using alternating colours
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                boolean even = ((row + col) & 1) == 0;
                terminal.setBg(even ? 30 : 60, even ? 30 : 60, even ? 30 : 60);
                terminal.put(col, row, ".");
            }
        }
        terminal.flush();

        Thread.sleep(3000);

        // Announce next test before sleeping (final message)
        terminal.reset();
        terminal.clear();
        terminal.put(0, 0, "Next test: Final clear and goodbye message");
        terminal.flush();
        Thread.sleep(3000);

        // 8. Clear screen again, print goodbye message with full intensity white
        terminal.setBg(255, 255, 255);
        terminal.setFg(0, 0, 0);
        terminal.clear();
        terminal.put(0, 0, "All tests completed.");
        terminal.flush();

        Thread.sleep(3000);

        terminal.reset();
        terminal.clear();
        terminal.flush();
    }
}
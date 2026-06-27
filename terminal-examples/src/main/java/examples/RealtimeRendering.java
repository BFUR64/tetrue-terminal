package examples;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

import java.util.concurrent.locks.LockSupport;

public final class RealtimeRendering {
    private static final long NS_PER_FRAME = 1_000_000_000L / 60;

    public static void main(String[] args) {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            long delta = 0;

            while (true) {
                long frameStart = System.nanoTime();

                KeyStroke keyStroke = terminal.poll();

                if (keyStroke != null) {
                    if (keyStroke.keyType() == KeyType.ESCAPE) {
                        break;
                    }
                }

                terminal.clear();
                terminal.put(0, 0, terminal.libraryInfo());
                terminal.put(0, 1, terminal.terminalInfo());
                terminal.put(0, 2, "Cols: " + terminal.xSize() + " | " + "Rows: " + terminal.ySize());
                terminal.put(0, 3, "FPS: " + Math.round(1_000_000_000.0d / delta));
                terminal.flush();

                long deadline = frameStart + NS_PER_FRAME;
                long now = System.nanoTime();

                long remaining = (deadline - now) / 2;
                if (remaining > 1_000_000) {
                    LockSupport.parkNanos(deadline - now);
                }

                while (now < deadline) {
                    Thread.onSpinWait();
                    now = System.nanoTime();
                }

                delta = System.nanoTime() - frameStart;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

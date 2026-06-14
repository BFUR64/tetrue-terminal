package examples;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;

import java.util.concurrent.locks.LockSupport;

public class LoopTest {
    private static final long NS_PER_FRAME = 1_000_000_000L / 60;

    public static void main(String[] args) {
        try (TerminalRuntime runtime = Terminal.builder().auto().buffered().build()) {
            Terminal terminal = runtime.terminal();

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
                terminal.flush();

                long deadline = frameStart + NS_PER_FRAME;
                long now = System.nanoTime();

                while (now < deadline) {
                    LockSupport.parkNanos(deadline - now);
                    now = System.nanoTime();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

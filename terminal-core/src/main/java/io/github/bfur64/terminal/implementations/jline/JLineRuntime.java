package io.github.bfur64.terminal.implementations.jline;

import io.github.bfur64.Versions;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.render.FrameBuilder;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jspecify.annotations.NullMarked;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@NullMarked
public final class JLineRuntime implements TerminalRuntime, TerminalEnvironment {
    private final Terminal terminal;
    private final org.jline.terminal.Terminal jlineTerminal;

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Thread pollingThread;

    public JLineRuntime() throws IOException {
        this.jlineTerminal = TerminalBuilder.builder().build();

        BlockingQueue<KeyStroke> inputQueue = new LinkedBlockingQueue<>(16);
        this.pollingThread = startPollingThread(inputQueue, new BindingReader(jlineTerminal.reader()), buildKeyMap());

        RendererBackend rendererBackend = new JLineBackend(jlineTerminal);

        this.terminal = new Terminal(this, new FrameBuilder(rendererBackend), new JLineInputSource(inputQueue));

        start();
    }

    private void start() {
        jlineTerminal.enterRawMode();
        jlineTerminal.puts(InfoCmp.Capability.cursor_invisible);
        jlineTerminal.puts(InfoCmp.Capability.enter_ca_mode);
        jlineTerminal.flush();
    }

    private Thread startPollingThread(BlockingQueue<KeyStroke> inputQueue, BindingReader bindingReader, KeyMap<KeyStroke> keyMap) {
        Thread pollingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && isRunning.get()) {
                try {
                    boolean ignored = inputQueue.offer(bindingReader.readBinding(keyMap), 5, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException | IOError e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        pollingThread.start();
        return pollingThread;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP") // Terminal is intentionally exposed by contract
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public int xSize() {
        // Workaround for JLine 4.2.1
        // Missing characters on the left edge appears under certain terminal operations for N
        // `JLineBackend` shifts entire rendering to the right after N-1 to skip the bad left edge
        // Affected Stack: `JlineRuntime` (this) -> `Terminal` -> `FrameBuilder` -> `JLineBackend`
        if (SystemUtils.IS_OS_WINDOWS) {
            return jlineTerminal.getColumns() - 1;
        }
        else {
            return jlineTerminal.getColumns();
        }
    }

    @Override
    public int ySize() {
        return jlineTerminal.getRows();
    }

    @Override
    public String terminalInfo() {
        return "JLine: " + Versions.JLINE;
    }

    @Override
    public void close() throws InterruptedException, IOException {
        isRunning.set(false);
        pollingThread.interrupt();
        pollingThread.join();

        jlineTerminal.puts(InfoCmp.Capability.cursor_visible);
        jlineTerminal.puts(InfoCmp.Capability.exit_ca_mode);
        jlineTerminal.flush();

        jlineTerminal.close();
    }

    private KeyMap<KeyStroke> buildKeyMap() {
        KeyMap<KeyStroke> map = new KeyMap<>();

        map.bind(new KeyStroke(KeyType.ARROW_UP), "\033[A");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN), "\033[B");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033[C");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT), "\033[D");
        map.bind(new KeyStroke(KeyType.HOME), "\033[H");
        map.bind(new KeyStroke(KeyType.END), "\033[F");

        map.bind(new KeyStroke(KeyType.ARROW_UP), "\033OA");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN), "\033OB");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033OC");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT), "\033OD");
        map.bind(new KeyStroke(KeyType.HOME), "\033OH");
        map.bind(new KeyStroke(KeyType.END), "\033OF");

        map.bind(new KeyStroke(KeyType.HOME), "\033[1~");
        map.bind(new KeyStroke(KeyType.END), "\033[4~");
        map.bind(new KeyStroke(KeyType.PAGE_UP), "\033[5~");
        map.bind(new KeyStroke(KeyType.PAGE_DOWN), "\033[6~");

        map.bind(new KeyStroke(KeyType.BACKSPACE), "\b"); // BS (8)
        map.bind(new KeyStroke(KeyType.BACKSPACE), "\177"); // DEL

        map.bind(new KeyStroke(KeyType.ENTER), "\r");

        map.bind(new KeyStroke(KeyType.ESCAPE), "\033");

        for (int c = 32; c < 127; c++) {
            map.bind(new KeyStroke((char) c), String.valueOf((char) c));
        }

        map.setAmbiguousTimeout(10);

        return map;
    }
}

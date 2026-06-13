package io.github.bfur64.terminal.v3.jline;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.v3.PipelineType;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.TerminalConfig;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.pipeline.BufferedPipeline;
import io.github.bfur64.terminal.v3.pipeline.ImmediatePipeline;
import io.github.bfur64.terminal.v3.pipeline.Pipeline;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.TerminalBuilder;
import org.jspecify.annotations.NullMarked;

import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@NullMarked
public final class JLineRuntime implements TerminalRuntime {
    private final Terminal terminal;
    private final org.jline.terminal.Terminal jlineTerminal;

    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private final Thread pollingThread;

    public JLineRuntime(TerminalConfig config) throws IOException {
        if (config.sizeOverride()) {
            throw new UnsupportedOperationException("JLine does not support size override");
        }

        this.jlineTerminal = TerminalBuilder.builder().system(true).dumb(false).build();

        Pipeline pipeline = config.pipelineType() == PipelineType.BUFFERED ?
            new BufferedPipeline(new JLineBackend(jlineTerminal, jlineTerminal.writer())) :
            new ImmediatePipeline(new JLineBackend(jlineTerminal, jlineTerminal.writer()));

        BlockingQueue<KeyStroke> inputQueue = new LinkedBlockingQueue<>(1);
        this.pollingThread = startPollingThread(inputQueue, new BindingReader(jlineTerminal.reader()), buildKeyMap());

        this.terminal = new Terminal(this, pipeline, new JLineInputSource(inputQueue));
    }

    private Thread startPollingThread(BlockingQueue<KeyStroke> inputQueue, BindingReader bindingReader, KeyMap<KeyStroke> keyMap) {
        Thread pollingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && isRunning.get()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    inputQueue.offer(bindingReader.readBinding(keyMap), 5, TimeUnit.MILLISECONDS);

                } catch (InterruptedException | IOError e) {
                    Thread.currentThread().interrupt();

                    break;
                }
            }
        });

        pollingThread.start();
        return pollingThread;
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public int xSize() {
        return jlineTerminal.getColumns();
    }

    @Override
    public int ySize() {
        return jlineTerminal.getRows();
    }

    @Override
    public String terminalInfo() {
        return "";
    }

    @Override
    public void close() throws InterruptedException {
        isRunning.set(false);
        pollingThread.interrupt();
        pollingThread.join();
    }

    private KeyMap<KeyStroke> buildKeyMap() {
        KeyMap<KeyStroke> map = new KeyMap<>();

        map.bind(new KeyStroke(KeyType.ARROW_UP),    "\033[A");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN),  "\033[B");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033[C");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT),  "\033[D");
        map.bind(new KeyStroke(KeyType.HOME), "\033[H");
        map.bind(new KeyStroke(KeyType.END), "\033[F");

        map.bind(new KeyStroke(KeyType.ARROW_UP),    "\033OA");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN),  "\033OB");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033OC");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT),  "\033OD");
        map.bind(new KeyStroke(KeyType.HOME), "\033OH");
        map.bind(new KeyStroke(KeyType.END), "\033OF");

        map.bind(new KeyStroke(KeyType.HOME), "\033[1~");
        map.bind(new KeyStroke(KeyType.END), "\033[4~");


        map.bind(new KeyStroke(KeyType.BACKSPACE), "\b"); // BS (8)
        map.bind(new KeyStroke(KeyType.BACKSPACE), "\177"); // DEL

        map.bind(new KeyStroke(KeyType.ENTER),     "\r");

        map.bind(new KeyStroke(KeyType.ESCAPE),    "\033");

        for (int c = 32; c < 127; c++) {
            map.bind(new KeyStroke((char) c), String.valueOf((char) c));
        }

        map.setAmbiguousTimeout(10);

        return map;
    }
}

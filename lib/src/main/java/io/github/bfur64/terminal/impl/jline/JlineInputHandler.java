package io.github.bfur64.terminal.impl.jline;

import io.github.bfur64.terminal.interfaces.InputHandler;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOError;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@NullMarked
class JlineInputHandler implements InputHandler {
    private final BindingReader bindingReader;
    private final KeyMap<KeyStroke> keyMap;

    private final BlockingQueue<KeyStroke> inputQueue = new LinkedBlockingQueue<>(1);
    private final AtomicBoolean isRunning = new AtomicBoolean(true);

    private @Nullable Thread pollingThread;

    public JlineInputHandler(Terminal terminal) {
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();
    }

    @Override
    public void start() {
        this.pollingThread = new Thread(() -> {
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
    }

    @Override
    public KeyStroke readInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new KeyStroke(KeyType.UNKNOWN);
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return inputQueue.poll();
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

    @Override
    public void close() {
        try {
            isRunning.set(false);

            if (pollingThread != null) {
                pollingThread.interrupt();
                pollingThread.join();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

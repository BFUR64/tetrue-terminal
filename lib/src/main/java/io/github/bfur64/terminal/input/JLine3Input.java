package io.github.bfur64.terminal.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOError;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JLine3Input implements TerminalInput {
    private final BindingReader bindingReader;
    private final KeyMap<KeyStroke> keyMap;

    private final BlockingQueue<KeyStroke> inputQueue = new LinkedBlockingQueue<>(1);

    public JLine3Input(Terminal terminal) {
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();

        Thread pollingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    inputQueue.offer(bindingReader.readBinding(keyMap), 5, TimeUnit.MILLISECONDS);

                } catch (Throwable e) {
                    Thread.currentThread().interrupt();

                    break;
                }
            }
        });

        pollingThread.start();
    }

    @Override
    public @NonNull KeyStroke readInput() {
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

        map.bind(new KeyStroke(KeyType.ARROW_UP),    "\033OA");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN),  "\033OB");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033OC");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT),  "\033OD");

        map.bind(new KeyStroke(KeyType.BACKSPACE), "\b");     // BS (8)
        map.bind(new KeyStroke(KeyType.BACKSPACE), "\177"); // DEL

        map.bind(new KeyStroke(KeyType.ENTER),     "\r");

        map.bind(new KeyStroke(KeyType.ESCAPE),    "\033");

        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                map.bind(new KeyStroke(c), String.valueOf(c));
            }
        }

        map.setAmbiguousTimeout(10);

        return map;
    }
}

package io.github.bfur64.terminal.jline;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.BlockingQueue;

@NullMarked
public final class JLineInputSource implements InputSource {
    private final BlockingQueue<KeyStroke> inputQueue;

    public JLineInputSource(BlockingQueue<KeyStroke> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public KeyStroke read() {
        try {
            return inputQueue.take();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return new KeyStroke(KeyType.UNKNOWN);
    }

    @Override
    public @Nullable KeyStroke poll() {
        return inputQueue.poll();
    }
}

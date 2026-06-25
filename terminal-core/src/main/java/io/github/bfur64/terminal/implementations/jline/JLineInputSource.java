package io.github.bfur64.terminal.implementations.jline;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.BlockingQueue;

@NullMarked
public final class JLineInputSource implements InputSource {
    private final BlockingQueue<KeyStroke> inputQueue;

    /**
     * Creates a {@link JLineInputSource} backed by the provided {@link BlockingQueue}.
     *
     * <p>This class does <b>not</b> own the supplied queue. The queue is a shared
     * communication channel between the polling thread and this input source.</p>
     *
     * <p>The queue is expected to remain valid for the entire lifetime of this instance.
     * If a thread waiting for input is interrupted during a blocking read operation,
     * the interrupt status will be restored before returning.</p>
     *
     * @param inputQueue Queue containing {@link KeyStroke} events produced by the runtime
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JLineInputSource(BlockingQueue<KeyStroke> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public KeyStroke read() {
        try {
            return inputQueue.take();
        }
        catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return new KeyStroke(KeyType.UNKNOWN);
    }

    @Override
    public @Nullable KeyStroke poll() {
        return inputQueue.poll();
    }
}

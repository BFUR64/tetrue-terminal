package io.github.bfur64.terminal.implementations.jline;

import io.github.bfur64.terminal.input.InputEvent;
import io.github.bfur64.terminal.input.Key;
import io.github.bfur64.terminal.input.KeyEvent;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.BlockingQueue;

@NullMarked
public final class JLineInputSource implements InputSource {
    private final BlockingQueue<InputEvent> inputQueue;

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
     * @param inputQueue Queue containing {@link InputEvent} events produced by the runtime
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JLineInputSource(BlockingQueue<InputEvent> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public InputEvent read() {
        try {
            return inputQueue.take();
        }
        catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        return new KeyEvent(Key.UNKNOWN);
    }

    @Override
    public @Nullable InputEvent poll() {
        return inputQueue.poll();
    }
}

package io.github.bfur64.terminal.jline;

import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.BufferedRenderer;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BufferedJLineBackend implements TerminalBackend {
    private final JlineInputHandler jlineInputHandler;
    private final JlineRendererHandler jlineRendererHandler;
    private BufferedRenderer bufferedRenderer;
    private ScheduledExecutorService bufferedResizePoller;

    public BufferedJLineBackend() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).dumb(false).build();

        jlineInputHandler = new JlineInputHandler(terminal);
        jlineRendererHandler = new JlineRendererHandler(terminal, terminal.writer());
    }

    @Override
    public @NonNull KeyStroke readInput() {
        return jlineInputHandler.readInput();
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return jlineInputHandler.pollInput();
    }

    @Override
    public void start() {
        jlineInputHandler.start();
        jlineRendererHandler.start();

        bufferedRenderer = new BufferedRenderer(
            jlineRendererHandler,
            jlineRendererHandler.getXSize(),
            jlineRendererHandler.getYSize()
        );

        bufferedResizePoller = Executors.newSingleThreadScheduledExecutor();
        bufferedResizePoller.scheduleAtFixedRate(() -> {
            int newWidth = jlineRendererHandler.getXSize();
            int newHeight = jlineRendererHandler.getYSize();
            bufferedRenderer.resize(newWidth, newHeight);
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void clearScreen() {
        bufferedRenderer.clearScreen();
    }

    @Override
    public void put(int x, int y, String out) {
        bufferedRenderer.put(x, y, out);
    }

    @Override
    public void flush() {
        bufferedRenderer.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        bufferedRenderer.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        bufferedRenderer.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        bufferedRenderer.resetColorAndStyle();
    }

    @Override
    public int getXSize() {
        return jlineRendererHandler.getXSize();
    }

    @Override
    public int getYSize() {
        return jlineRendererHandler.getYSize();
    }

    @Override
    public String getTerminalInfo() {
        return "JLine: " + Config.jlineVersion;
    }

    @Override
    public void close() throws IOException {
        jlineInputHandler.close();
        bufferedResizePoller.close();
        jlineRendererHandler.close();
    }
}

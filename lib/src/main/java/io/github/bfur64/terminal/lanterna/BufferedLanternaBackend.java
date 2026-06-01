package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.BufferedRenderer;
import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NullMarked
public class BufferedLanternaBackend implements TerminalBackend {
    private final LanternaInputHandler lanternaInputHandler;
    private final LanternaRendererHandler lanternaRendererHandler;
    private final BufferedRenderer bufferedRenderer;
    private @Nullable ScheduledExecutorService bufferedResizePoller;

    public BufferedLanternaBackend() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        lanternaInputHandler = new LanternaInputHandler(terminal);
        lanternaRendererHandler = new LanternaRendererHandler(terminal, terminal.newTextGraphics());
        bufferedRenderer = new BufferedRenderer(lanternaRendererHandler);
    }

    @Override
    public KeyStroke readInput() {
        return lanternaInputHandler.readInput();
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return lanternaInputHandler.pollInput();
    }

    @Override
    public void start() {
        lanternaInputHandler.start();
        lanternaRendererHandler.start();

        bufferedResizePoller = Executors.newSingleThreadScheduledExecutor();
        bufferedResizePoller.scheduleAtFixedRate(() -> {
            int newWidth = lanternaRendererHandler.getXSize();
            int newHeight = lanternaRendererHandler.getYSize();
            bufferedRenderer.resize(newWidth, newHeight);
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void clearScreen() {
        lanternaRendererHandler.clearScreen();
    }

    @Override
    public void put(int x, int y, String out) {
        lanternaRendererHandler.put(x, y, out);
    }

    @Override
    public void flush() {
        lanternaRendererHandler.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        lanternaRendererHandler.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        lanternaRendererHandler.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        lanternaRendererHandler.resetColorAndStyle();
    }

    @Override
    public int getXSize() {
        return lanternaRendererHandler.getXSize();
    }

    @Override
    public int getYSize() {
        return lanternaRendererHandler.getYSize();
    }

    @Override
    public String getTerminalInfo() {
        return "Lanterna: " + Config.lanternaVersion;
    }

    @Override
    public void close() throws IOException {
        lanternaInputHandler.close();
        lanternaRendererHandler.close();
    }
}

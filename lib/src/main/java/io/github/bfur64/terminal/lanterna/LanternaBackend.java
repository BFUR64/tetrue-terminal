package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.utils.TerminalBackend;
import io.github.bfur64.terminal.input.KeyStroke;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

public class LanternaBackend implements TerminalBackend {
    private final LanternaInputHandler lanternaInputHandler;
    private final LanternaRendererHandler lanternaRendererHandler;

    public LanternaBackend() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        lanternaInputHandler = new LanternaInputHandler(terminal);
        lanternaRendererHandler = new LanternaRendererHandler(terminal, terminal.newTextGraphics());
    }

    @Override
    public @NonNull KeyStroke readInput() {
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

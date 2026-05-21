package io.github.bfur64.terminal.jline3;

import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import io.github.bfur64.terminal.input.KeyStroke;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

public class JLine3Backend implements TerminalBackend {
    private final JLine3InputHandler jLine3InputHandler;
    private final JLine3RendererHandler jLine3RendererHandler;

    public JLine3Backend(boolean isTermux) throws IOException {
        if (isTermux) {
            System.setProperty("org.jline.terminal.provider", "exec");
            System.setProperty("java.awt.headless", "true");
            System.setProperty("file.encoding", "UTF-8");

        }

        Terminal terminal = TerminalBuilder.builder().system(true).dumb(false).build();

        jLine3InputHandler = new JLine3InputHandler(terminal);
        jLine3RendererHandler = new JLine3RendererHandler(terminal, terminal.writer());
    }

    @Override
    public @NonNull KeyStroke readInput() {
        return jLine3InputHandler.readInput();
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return jLine3InputHandler.pollInput();
    }

    @Override
    public void start() {
        jLine3InputHandler.start();
        jLine3RendererHandler.start();
    }

    @Override
    public void clearScreen() {
        jLine3RendererHandler.clearScreen();
    }

    @Override
    public void put(int x, int y, String out) {
        jLine3RendererHandler.put(x, y, out);
    }

    @Override
    public void flush() {
        jLine3RendererHandler.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        jLine3RendererHandler.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        jLine3RendererHandler.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        jLine3RendererHandler.resetColorAndStyle();
    }

    @Override
    public int getXSize() {
        return jLine3RendererHandler.getXSize();
    }

    @Override
    public int getYSize() {
        return jLine3RendererHandler.getYSize();
    }

    @Override
    public String getTerminalInfo() {
        return "JLine3: " + Config.jline3Version;
    }

    @Override
    public void close() throws IOException {
        jLine3InputHandler.close();
        jLine3RendererHandler.close();
    }
}

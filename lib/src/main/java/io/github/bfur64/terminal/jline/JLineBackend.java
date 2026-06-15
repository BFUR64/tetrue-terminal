package io.github.bfur64.terminal.jline;

import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.SymbolBuffer;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import io.github.bfur64.terminal.input.KeyStroke;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public class JLineBackend implements TerminalBackend {
    private final JlineInputHandler jlineInputHandler;
    private final JlineRendererHandler jlineRendererHandler;

    public JLineBackend() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).dumb(false).build();

        jlineInputHandler = new JlineInputHandler(terminal);
        jlineRendererHandler = new JlineRendererHandler(terminal, terminal.writer());
    }

    @Override
    public KeyStroke readInput() {
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
    }

    @Override
    public void clearScreen() {
        jlineRendererHandler.clearScreen();
    }

    @Override
    public void put(int x, int y, String out) {
        jlineRendererHandler.put(x, y, out);
    }

    @Override
    public void flush() {
        jlineRendererHandler.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        jlineRendererHandler.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        jlineRendererHandler.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        jlineRendererHandler.resetColorAndStyle();
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
        jlineRendererHandler.close();
    }

    @Override
    public void flushBuffer(SymbolBuffer buffer) {
        jlineRendererHandler.flushBuffer(buffer);
    }
}

package io.github.bfur64.terminal;

import io.github.bfur64.terminal.input.JLine3Input;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.LanternaInput;
import io.github.bfur64.terminal.input.TerminalInput;
import io.github.bfur64.terminal.render.JLine3Renderer;
import io.github.bfur64.terminal.render.LanternaRenderer;
import io.github.bfur64.terminal.render.TerminalRenderer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

public class Terminal implements TerminalRenderer, TerminalInput, Closeable {
    private final TerminalRenderer renderer;
    private final TerminalInput input;

    private Terminal(TerminalRenderer renderer, TerminalInput input) {
        this.renderer = renderer;
        this.input = input;
    }

    @SuppressWarnings("unused")
    public static Terminal build() throws IOException {
        if (isTermux()) {
            LanternaRenderer renderer = new LanternaRenderer();
            LanternaInput input = new LanternaInput(renderer.getTerminal());
            return new Terminal(renderer, input);
        }
        else {
            JLine3Renderer renderer = new JLine3Renderer();
            JLine3Input input = new JLine3Input(renderer.getTerminal());
            return new Terminal(renderer, input);
        }
    }

    private static boolean isTermux() {
        String prefix = System.getenv("PREFIX");

        return (prefix != null &&
            prefix.contains("termux")) ||
            System.getenv("TERMUX_VERSION") != null;
    }

    @Override
    public void clearScreen() {
        renderer.clearScreen();
    }

    @Override
    public void putString(int x, int y, String out) {
        renderer.putString(x, y, out);
    }

    @Override
    public void flush() {
        renderer.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        renderer.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        renderer.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        renderer.resetColorAndStyle();
    }

    @Override
    public int getXSize() {
        return renderer.getXSize();
    }

    @Override
    public int getYSize() {
        return renderer.getYSize();
    }

    @Override
    public String getTerminalInfo() {
        return renderer.getTerminalInfo();
    }

    @Override
    public @NonNull KeyStroke readInput() {
        return input.readInput();
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return input.pollInput();
    }

    @Override
    public void close() throws IOException {
        renderer.close();
        input.close();
    }
}

package io.github.bfur64.terminal;

import io.github.bfur64.terminal.jline.JLineBackend;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import io.github.bfur64.terminal.lanterna.LanternaBackend;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public class Terminal implements TerminalBackend {
    private final TerminalBackend terminalBackend;
    private boolean started;
    private boolean closed;

    Terminal(TerminalBackend terminalBackend) {
        this.terminalBackend = terminalBackend;
    }

    public static TerminalBackend auto() throws IOException {
        if (isTermux()) {
            return new LanternaBackend();
        }
        else {
            return new JLineBackend();
        }
    }

    public static TerminalBackend lanterna() throws IOException {
        return new LanternaBackend();
    }

    public static TerminalBackend jline() throws IOException {
        return new JLineBackend();
    }

    private static boolean isTermux() {
        String prefix = System.getenv("PREFIX");

        return (prefix != null &&
            prefix.contains("termux")) ||
            System.getenv("TERMUX_VERSION") != null;
    }

    @Override
    public KeyStroke readInput() {
        return terminalBackend.readInput();
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        return terminalBackend.pollInput();
    }

    @Override
    public void start() {
        if (started) return;
        started = true;
        terminalBackend.start();
    }

    @Override
    public void clearScreen() {
        terminalBackend.clearScreen();
    }

    @Override
    public void put(int x, int y, String out) {
        terminalBackend.put(x, y, out);
    }

    @Override
    public void flush() {
        terminalBackend.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        terminalBackend.setForegroundColor(r, g, b);
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        terminalBackend.setBackgroundColor(r, g, b);
    }

    @Override
    public void resetColorAndStyle() {
        terminalBackend.resetColorAndStyle();
    }

    @Override
    public int getXSize() {
        return terminalBackend.getXSize();
    }

    @Override
    public int getYSize() {
        return terminalBackend.getYSize();
    }

    @Override
    public String getTerminalInfo() {
        return terminalBackend.getTerminalInfo();
    }

    public static String getLibraryInfo() {
        return "Tetrue Terminal: " + Config.tetrueTerminalVersion;
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        terminalBackend.close();
    }

    @Override
    public void flushBuffer(SymbolBuffer buffer) {
        terminalBackend.flushBuffer(buffer);
    }
}

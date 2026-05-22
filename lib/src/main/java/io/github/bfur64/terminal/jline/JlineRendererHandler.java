package io.github.bfur64.terminal.jline;

import io.github.bfur64.terminal.interfaces.RendererHandler;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.InfoCmp.Capability;

import java.io.IOException;
import java.io.PrintWriter;

class JlineRendererHandler implements RendererHandler {
    private final Terminal terminal;
    private final PrintWriter printWriter;

    public JlineRendererHandler(Terminal terminal, PrintWriter printWriter) {
        this.terminal = terminal;
        this.printWriter = printWriter;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @Override
    public void start() {
        terminal.enterRawMode();
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        terminal.puts(InfoCmp.Capability.enter_ca_mode);
        terminal.flush();
    }

    @Override
    public void clearScreen() {
        terminal.puts(Capability.clear_screen);
    }

    @Override
    public void put(int x, int y, String out) {
        terminal.puts(Capability.cursor_address, y, x);
        printWriter.print(out);
    }

    @Override
    public void flush() {
        printWriter.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        printWriter.print(String.format("\u001b[38;2;%s;%s;%sm", r, g, b));
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        printWriter.print(String.format("\u001b[48;2;%s;%s;%sm", r, g, b));
    }

    @Override
    public void resetColorAndStyle() {
        printWriter.print("\u001b[0m");
        flush();
    }

    @Override
    public int getXSize() {
        return terminal.getSize().getColumns();
    }

    @Override
    public int getYSize() {
        return terminal.getSize().getRows();
    }

    @Override
    public void close() throws IOException {
        terminal.puts(Capability.cursor_visible);
        terminal.puts(Capability.exit_ca_mode);
        terminal.flush();

        terminal.close();
    }
}

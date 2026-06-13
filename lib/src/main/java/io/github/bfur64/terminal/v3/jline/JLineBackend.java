package io.github.bfur64.terminal.v3.jline;

import io.github.bfur64.terminal.v3.commands.*;
import io.github.bfur64.terminal.v3.interfaces.RendererBackend;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;
import org.jspecify.annotations.NullMarked;

import java.io.PrintWriter;

@NullMarked
public final class JLineBackend implements RendererBackend {
    private final Terminal terminal;
    private final PrintWriter printWriter;

    public JLineBackend(Terminal terminal, PrintWriter printWriter) {
        this.terminal = terminal;
        this.printWriter = printWriter;
    }

    @Override
    public void execute(Command command) {
        switch (command) {
            case Clear clear -> clear();
            case Flush flush -> flush();
            case Put put -> put(put);
            case Reset reset -> reset();
            case SetBg setBg -> setBg(setBg);
            case SetFg setFg -> setFg(setFg);
        }
    }

    private void clear() {
        terminal.puts(Capability.clear_screen);
    }

    private void flush() {
        printWriter.flush();
    }

    private void put(Put put) {
        terminal.puts(Capability.cursor_address, put.x(), put.y());
        printWriter.print(put.text());
    }

    private void reset() {
        printWriter.print("\u001b[0m");
        flush();
    }

    private void setFg(SetFg setFg) {
        printWriter.print(String.format("\u001b[38;2;%s;%s;%sm", setFg.r(), setFg.g(), setFg.b()));
    }

    private void setBg(SetBg setBg) {
        printWriter.print(String.format("\u001b[48;2;%s;%s;%sm", setBg.r(), setBg.g(), setBg.b()));
    }
}

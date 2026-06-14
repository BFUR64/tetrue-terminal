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
            case Clear ignored -> terminal.puts(Capability.clear_screen);
            case Flush ignored -> printWriter.flush();
            case Put put -> {
                terminal.puts(Capability.cursor_address, put.y(), put.x());
                printWriter.print(put.text());
            }
            case Reset ignored -> {
                printWriter.print("\u001b[0m");
                execute(new Flush());
            }
            case SetBg setBg -> printWriter.print(String.format("\u001b[48;2;%s;%s;%sm", setBg.r(), setBg.g(), setBg.b()));
            case SetFg setFg -> printWriter.print(String.format("\u001b[38;2;%s;%s;%sm", setFg.r(), setFg.g(), setFg.b()));
        }
    }
}

package io.github.bfur64.terminal.jline;

import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.SGR;
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
            case OffSGR offSGR -> printWriter.print(convertSGR(offSGR.SGR(), false));
            case OnSGR onSGR -> printWriter.print(convertSGR(onSGR.SGR(), true));
            case Put put -> {
                terminal.puts(Capability.cursor_address, put.y(), put.x());
                printWriter.print(put.text());
            }
            case Reset ignored -> printWriter.print("\u001b[0m");
            case SetBg setBg -> printWriter.print(String.format("\u001b[48;2;%s;%s;%sm", setBg.r(), setBg.g(), setBg.b()));
            case SetFg setFg -> printWriter.print(String.format("\u001b[38;2;%s;%s;%sm", setFg.r(), setFg.g(), setFg.b()));
        }
    }

    private String convertSGR(SGR SGR, boolean enabled) {
        if (enabled) {
            return switch (SGR) {
                case BOLD -> "\u001B[1m";
                case REVERSE -> "\u001B[7m";
                case UNDERLINE -> "\u001B[4m";
                case ITALIC -> "\u001B[3m";
                case STRIKETHROUGH -> "\u001B[9m";
            };
        }
        else {
            return switch (SGR) {
                case BOLD -> "\u001B[22m";
                case REVERSE -> "\u001B[27m";
                case UNDERLINE -> "\u001B[24m";
                case ITALIC -> "\u001B[23m";
                case STRIKETHROUGH -> "\u001B[29m";
            };
        }
    }
}

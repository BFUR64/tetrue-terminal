package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;

import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

@NullMarked
public final class LanternaBackend implements RendererBackend {
    private final Terminal terminal;
    private final TextGraphics textGraphics;

    public LanternaBackend(Terminal terminal, TextGraphics textGraphics) {
        this.terminal = terminal;
        this.textGraphics = textGraphics;
    }

    @Override
    public void execute(Command command) {
        try {
            switch (command) {
                case Clear ignored -> terminal.clearScreen();
                case Flush ignored -> terminal.flush();
                case OffSGR offSGR -> terminal.disableSGR(convertSGR(offSGR.SGR()));
                case OnSGR onSGR -> terminal.enableSGR(convertSGR(onSGR.SGR()));
                case Put put -> textGraphics.putString(put.x(), put.y(), put.text());
                case Reset ignored -> terminal.resetColorAndSGR();
                case SetBg setBg -> textGraphics.setBackgroundColor(new TextColor.RGB(setBg.r(), setBg.g(), setBg.b()));
                case SetFg setFg -> textGraphics.setForegroundColor(new TextColor.RGB(setFg.r(), setFg.g(), setFg.b()));
            }
        }
        catch (IOException ignored) {}
    }

    private com.googlecode.lanterna.SGR convertSGR(SGR SGR) {
        return switch (SGR) {
            case BOLD -> com.googlecode.lanterna.SGR.BOLD;
            case REVERSE -> com.googlecode.lanterna.SGR.REVERSE;
            case UNDERLINE -> com.googlecode.lanterna.SGR.UNDERLINE;
            case ITALIC -> com.googlecode.lanterna.SGR.ITALIC;
            case STRIKETHROUGH -> com.googlecode.lanterna.SGR.CROSSED_OUT;
        };
    }
}

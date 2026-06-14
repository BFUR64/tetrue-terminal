package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.commands.*;
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
                case Put put -> textGraphics.putString(put.x(), put.y(), put.text());
                case Reset ignored -> terminal.resetColorAndSGR();
                case SetBg setBg -> textGraphics.setForegroundColor(new TextColor.RGB(setBg.r(), setBg.g(), setBg.b()));
                case SetFg setFg -> textGraphics.setForegroundColor(new TextColor.RGB(setFg.r(), setFg.g(), setFg.b()));
            }
        }
        catch (IOException ignored) {}
    }
}

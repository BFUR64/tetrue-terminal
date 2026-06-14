package io.github.bfur64.terminal.v3.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import io.github.bfur64.terminal.v3.interfaces.RendererBackend;
import io.github.bfur64.terminal.v3.commands.*;
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
        switch(command) {
            case Clear ignored -> clear();
            case Flush ignored -> flush();
            case Put put -> put(put);
            case Reset ignored -> reset();
            case SetBg setBg -> setBg(setBg);
            case SetFg setFg -> setFg(setFg);
        }
    }

    private void clear() {
        try {
            terminal.clearScreen();
        }
        catch (IOException ignored) {}
    }

    private void flush() {
        try {
            terminal.flush();
        }
        catch (IOException ignored) {}
    }

    private void put(Put put) {
        textGraphics.putString(put.x(), put.y(), put.text());
    }

    private void reset() {
        try {
            terminal.resetColorAndSGR();
        }
        catch (IOException ignored) {}
    }

    private void setBg(SetBg setBg) {
        textGraphics.setForegroundColor(new TextColor.RGB(setBg.r(), setBg.g(), setBg.b()));
    }

    private void setFg(SetFg setFg) {
        textGraphics.setForegroundColor(new TextColor.RGB(setFg.r(), setFg.g(), setFg.b()));
    }
}

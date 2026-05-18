package io.github.bfur64.terminal.render;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.*;
import java.io.IOException;

public class LanternaRenderer implements TerminalRenderer {
    private final Terminal terminal;
    private final TextGraphics textGraphics;

    public LanternaRenderer() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setCursorVisible(false);

        textGraphics = terminal.newTextGraphics();

        terminal.enterPrivateMode();
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @Override
    public void clearScreen() {
        try {
            terminal.clearScreen();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putString(int x, int y, String out) {
        textGraphics.putString(x, y, out);
    }

    @Override
    public void flush() {
        try {
            terminal.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        textGraphics.setForegroundColor(TextColor.Indexed.fromRGB(r, g, b));
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        textGraphics.setBackgroundColor(TextColor.Indexed.fromRGB(r, g, b));
    }

    @Override
    public void resetColorAndStyle() {
        try {
            setForegroundColor(255, 255, 255);
            setBackgroundColor(0, 0, 0);

            terminal.resetColorAndSGR();
            flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getXSize() {
        return textGraphics.getSize().getColumns();
    }

    @Override
    public int getYSize() {
        return textGraphics.getSize().getRows();
    }

    @Override
    public String getCurrentTerminal() {
        return "Lanterna";
    }

    @Override
    public void close() throws IOException {
        terminal.exitPrivateMode();
        terminal.close();
    }
}

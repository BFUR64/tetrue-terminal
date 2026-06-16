package io.github.bfur64.terminal.impl.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.Color;
import io.github.bfur64.terminal.Size;
import io.github.bfur64.terminal.Symbol;
import io.github.bfur64.terminal.SymbolBuffer;
import io.github.bfur64.terminal.interfaces.RendererHandler;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
class LanternaRendererHandler implements RendererHandler {
    private final Terminal terminal;
    private final TextGraphics textGraphics;

    public LanternaRendererHandler(Terminal terminal, TextGraphics textGraphics) {
        this.terminal = terminal;
        this.textGraphics = textGraphics;
    }

    @Override
    public void start() {
        try {
            terminal.setCursorVisible(false);
            terminal.enterPrivateMode();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Size getSize() {
        final TerminalSize size = textGraphics.getSize();
        return Size.of(size.getRows(), size.getColumns());
    }

    @Override
    public int getHeight() {
        return textGraphics.getSize().getRows();
    }

    @Override
    public int getWidth() {
        return textGraphics.getSize().getColumns();
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
    public void put(int x, int y, String out) {
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
    public void close() throws IOException {
        terminal.exitPrivateMode();
        terminal.close();
    }

    @Override
    public void flushBuffer(SymbolBuffer buffer) {
        final int bufferWidth = buffer.getWidth();
        final int bufferHeight = buffer.getHeight();

        if (bufferHeight <= 0 || bufferWidth <= 0) return;

        for (int y = 0; y < bufferHeight; y++) {
            for (int x = 0; x < bufferWidth; x++) {
                final Symbol symbol = buffer.get(x, y);

                final Color foregroundColor = symbol.foregroundColor();
                final Color backgroundColor = symbol.backgroundColor();

                textGraphics.setForegroundColor(new TextColor.RGB(
                        foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue()));
                textGraphics.setBackgroundColor(new TextColor.RGB(
                        backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));

                textGraphics.setCharacter(x, y, symbol.value());
            }
        }

        flush();
    }
}

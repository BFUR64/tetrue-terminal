package io.github.bfur64.terminal.render;

import java.io.Closeable;
import java.io.IOException;

public interface TerminalRenderer extends Closeable {
    void clearScreen();
    void putString(int x, int y, String out);
    void flush();

    void setForegroundColor(int r, int g, int b);
    void setBackgroundColor(int r, int g, int b);
    void resetColorAndStyle();

    int getXSize();
    int getYSize();

    String getTerminalInfo();

    @Override
    void close() throws IOException;
}

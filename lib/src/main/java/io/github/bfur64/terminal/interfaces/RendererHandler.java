package io.github.bfur64.terminal.interfaces;

import java.io.Closeable;

public interface RendererHandler extends Closeable {
    void start();

    void clearScreen();
    void put(int x, int y, String out);
    void flush();

    void setForegroundColor(int r, int g, int b);
    void setBackgroundColor(int r, int g, int b);
    void resetColorAndStyle();

    int getXSize();
    int getYSize();
}

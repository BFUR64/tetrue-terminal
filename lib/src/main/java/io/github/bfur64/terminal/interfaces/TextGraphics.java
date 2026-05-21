package io.github.bfur64.terminal.interfaces;

public interface TextGraphics {
    void clearScreen();
    void put(int x, int y, String out);
    void flush();

    void setForegroundColor(int r, int g, int b);
    void setBackgroundColor(int r, int g, int b);
    void resetColorAndStyle();
}

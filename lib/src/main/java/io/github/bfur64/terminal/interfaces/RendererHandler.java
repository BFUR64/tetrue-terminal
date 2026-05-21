package io.github.bfur64.terminal.interfaces;

import java.io.Closeable;

public interface RendererHandler extends TextGraphics, Closeable {
    void start();
    int getXSize();
    int getYSize();
}

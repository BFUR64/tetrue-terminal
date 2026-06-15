package io.github.bfur64.terminal.interfaces;

import org.jspecify.annotations.NullMarked;

import java.io.Closeable;

@NullMarked
public interface RendererHandler extends TextGraphics, Closeable, Buffered {
    void start();
    int getXSize();
    int getYSize();
}

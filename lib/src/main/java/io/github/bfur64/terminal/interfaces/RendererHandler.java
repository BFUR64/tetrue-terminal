package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.Size;
import org.jspecify.annotations.NullMarked;

import java.io.Closeable;

@NullMarked
public interface RendererHandler extends TextGraphics, Closeable, Buffered {

    void start();
    Size getSize();
    int getHeight();
    int getWidth();
}

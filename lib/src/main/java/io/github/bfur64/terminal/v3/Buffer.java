package io.github.bfur64.terminal.v3;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Buffer {
    private final Cell[][] prevBuffer;
    private final Cell[][] nextBuffer;
    private final int width;
    private final int height;
    private volatile boolean fullRedraw;

    private Buffer(Cell[][] prevBuffer, Cell[][] nextBuffer, int width, int height, boolean fullRedraw) {
        this.prevBuffer = prevBuffer;
        this.nextBuffer = nextBuffer;
        this.width = width;
        this.height = height;
        this.fullRedraw = fullRedraw;
    }
}

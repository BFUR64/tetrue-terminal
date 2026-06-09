package io.github.bfur64.terminal;

import io.github.bfur64.terminal.interfaces.TextGraphics;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BufferedRenderer implements TextGraphics {
    private final TextGraphics renderer;

    private volatile Buffer buffer;

    private int currFgR = -1, currFgG = -1, currFgB = -1;
    private int currBgR = -1, currBgG = -1, currBgB = -1;

    public BufferedRenderer(TextGraphics renderer) {
        this.renderer = renderer;

        allocateBuffers(0, 0, false);
    }

    private void allocateBuffers(int width, int height, boolean localFullRedraw) {
        Cell[][] prevBuffer = new Cell[height][width];
        Cell[][] nextBuffer = new Cell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                prevBuffer[y][x] = new Cell();
                nextBuffer[y][x] = new Cell();
            }
        }

        buffer = new Buffer(prevBuffer, nextBuffer, width, height, localFullRedraw);
    }

    public void resize(int newWidth, int newHeight) {
        Buffer current = buffer;

        if (newWidth == current.width && newHeight == current.height) return;

        allocateBuffers(newWidth, newHeight, true);
    }

    @Override
    public void clearScreen() {
        for (Cell[] row : buffer.nextBuffer) {
            for (Cell col : row) {
                col.reset();
            }
        }
    }

    @Override
    public void put(int x, int y, String out) {
        Buffer current = buffer;

        if (x < 0 || x >= current.width || y < 0 || y >= current.height) return;
        if (out.isEmpty()) return;

        for (int i = 0; i < out.length(); i++) {
            if (x + (i + 1) > current.width) break;

            Cell cell = current.nextBuffer[y][x + i];

            cell.ch = out.charAt(i);
            cell.fgR = currFgR; cell.fgG = currFgG; cell.fgB = currFgB;
            cell.bgR = currBgR; cell.bgG = currBgG; cell.bgB = currBgB;
        }
    }

    @Override
    public void flush() {
        Buffer current = buffer;

        if (current.fullRedraw) {
            renderer.clearScreen();
            current.fullRedraw = false;
        }

        boolean termFgDefault = true;
        int termFgR = -1, termFgG = -1, termFgB = -1;

        boolean termBgDefault = true;
        int termBgR = -1, termBgG = -1, termBgB = -1;

        for (int y = 0; y < current.height; y++) {
            for (int x = 0; x < current.width; x++) {
                Cell next = current.nextBuffer[y][x];
                Cell prev = current.prevBuffer[y][x];

                if (!next.equals(prev)) {
                    // --- Foreground ---
                    if (next.isFgDefault()) {
                        if (!termFgDefault) {
                            renderer.resetColorAndStyle();
                            termFgDefault = true;
                        }
                    } else {
                        if (termFgDefault || next.fgR != termFgR || next.fgG != termFgG || next.fgB != termFgB
                        ) {
                            renderer.setForegroundColor(next.fgR, next.fgG, next.fgB);
                            termFgDefault = false;
                            termFgR = next.fgR; termFgG = next.fgG; termFgB = next.fgB;
                        }
                    }

                    // --- Background ---
                    if (next.isBgDefault()) {
                        if (!termBgDefault) {
                            renderer.resetColorAndStyle();
                            termBgDefault = true;
                        }
                    } else {
                        if (termBgDefault || next.bgR != termBgR || next.bgG != termBgG || next.bgB != termBgB
                        ) {
                            renderer.setBackgroundColor(next.bgR, next.bgG, next.bgB);
                            termBgDefault = false;
                            termBgR = next.bgR; termBgG = next.bgG; termBgB = next.bgB;
                        }
                    }

                    renderer.put(x, y, String.valueOf(next.ch));
                    prev.copyFrom(next);
                }
            }
        }

        renderer.resetColorAndStyle();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        currFgR = r; currFgG = g; currFgB = b;
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        currBgR = r; currBgG = g; currBgB = b;
    }

    @Override
    public void resetColorAndStyle() {
        currFgR = currFgG = currFgB = -1;
        currBgR = currBgG = currBgB = -1;
    }

    private static class Cell {
        char ch = ' ';
        int fgR = -1, fgG = -1, fgB = -1;
        int bgR = -1,   bgG = -1,   bgB = -1;

        boolean equals(Cell other) {
            return ch == other.ch
                && fgR == other.fgR && fgG == other.fgG && fgB == other.fgB
                && bgR == other.bgR && bgG == other.bgG && bgB == other.bgB;
        }

        void copyFrom(Cell other) {
            this.ch = other.ch;
            this.fgR = other.fgR; this.fgG = other.fgG; this.fgB = other.fgB;
            this.bgR = other.bgR; this.bgG = other.bgG; this.bgB = other.bgB;
        }

        void reset() {
            ch = ' ';
            fgR=fgG=fgB=-1;
            bgR=bgG=bgB=-1;
        }

        boolean isFgDefault() { return fgR == -1; }
        boolean isBgDefault() { return bgR == -1; }
    }

    private static final class Buffer {
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
}

package io.github.bfur64.terminal;

public class SymbolBuffer {

    private final Symbol[][] buffer;

    public SymbolBuffer(int height, int width) {
        buffer = new Symbol[height][width];
    }

    public void set(int x, int y, Symbol symbol) {
        buffer[y][x] = symbol;
    }

    public Symbol[][] get() {
        return buffer;
    }

    public int getHeight() {
        return buffer.length;
    }

    public int getWidth() {
        return buffer[0] != null ? buffer[0].length : 0;
    }
}

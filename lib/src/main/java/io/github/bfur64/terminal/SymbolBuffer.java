package io.github.bfur64.terminal;

public class SymbolBuffer {

    private final Symbol[][] buffer;

    public SymbolBuffer(int height, int width) {
        buffer = new Symbol[height][width];
    }

    public SymbolBuffer(Size size) {
        buffer = new Symbol[size.height()][size.width()];
    }

    public void set(int x, int y, Symbol symbol) {
        buffer[y][x] = symbol;
    }

    public Symbol[][] get() {
        return buffer;
    }

    public Symbol get(int x, int y) {
        return buffer[y][x];
    }

    public int getHeight() {
        return buffer.length;
    }

    public int getWidth() {
        return buffer.length > 0 && buffer[0] != null ? buffer[0].length : 0;
    }
}

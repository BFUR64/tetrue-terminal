package io.github.bfur64.terminal.render;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class Frame {
    private int bufferXSize;
    private int bufferYSize;

    private @Nullable Symbol[] buffer = new Symbol[0];

    public void copyFrame(Frame source) {
        bufferXSize = source.bufferXSize;
        bufferYSize = source.bufferYSize;
        newBuffer();
        System.arraycopy(source.buffer, 0, buffer, 0, bufferXSize * bufferYSize);
    }

    public void newBuffer() {
        buffer = new Symbol[bufferYSize * bufferXSize];
    }

    public void resizeBuffer(int newXSize, int newYSize) {
        newXSize = Math.max(0, newXSize);
        newYSize = Math.max(0, newYSize);

        @Nullable Symbol[] localBuffer = new Symbol[newYSize * newXSize];

        int colsToCopy = Math.min(bufferXSize, newXSize);
        int rowsToCopy = Math.min(bufferYSize, newYSize);

        for (int y = 0; y < rowsToCopy; y++) {
            System.arraycopy(
                buffer,y * bufferXSize,
                localBuffer, y * newXSize,
                colsToCopy
            );
        }

        bufferXSize = newXSize;
        bufferYSize = newYSize;
        buffer = localBuffer;
    }

    public boolean rowChanged(Frame otherFrame, int y) {
        if (bufferXSize != otherFrame.bufferXSize) return true;

        for (int x = 0; x < bufferXSize; x++) {
            if (!Objects.equals(getSymbol(x, y), otherFrame.getSymbol(x, y))) return true;
        }

        return false;
    }

    public int getBufferXSize() {
        return bufferXSize;
    }

    public int getBufferYSize() {
        return bufferYSize;
    }

    public @Nullable Symbol getSymbol(int x, int y) {
        return buffer[index(x, y)];
    }

    public void setSymbol(int x, int y, @Nullable Symbol symbol) {
        buffer[index(x, y)] = symbol;
    }

    private int index(int x, int y) {
        return y * bufferXSize + x;
    }
}

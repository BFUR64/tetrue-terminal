package io.github.bfur64.terminal.v3;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Cell {
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

package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.render.Symbol;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RendererBackend {
    void draw(Symbol[][] frame, int termXSize, int termYSize);
}

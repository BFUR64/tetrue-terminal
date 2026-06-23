package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.render.Symbol;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface RendererBackend {
    void draw(@Nullable Symbol[][] frame, int termXSize, int termYSize);
}

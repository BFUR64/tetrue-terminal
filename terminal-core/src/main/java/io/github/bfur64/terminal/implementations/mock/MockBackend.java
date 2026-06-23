package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.render.Symbol;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class MockBackend implements RendererBackend {
    @Override
    public void draw(@Nullable Symbol[][] frame, int termXSize, int termYSize) {}
}

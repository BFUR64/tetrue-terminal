package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.render.Symbol;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockBackend implements RendererBackend {
    @Override
    public void draw(Symbol[][] frame, int termXSize, int termYSize) {}
}

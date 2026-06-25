package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.render.Frame;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockBackend implements RendererBackend {
    @Override
    public void draw(Frame frame, int termXSize, int termYSize) {}
}

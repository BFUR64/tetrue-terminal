package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.render.Frame;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RendererBackend {
    void draw(Frame frame, int termXSize, int termYSize);
}

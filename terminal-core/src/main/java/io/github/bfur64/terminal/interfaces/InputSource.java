package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.input.InputEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface InputSource {
    InputEvent read();
    @Nullable InputEvent poll();
}

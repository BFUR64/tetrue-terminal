package io.github.bfur64.terminal.utils;

import io.github.bfur64.terminal.input.KeyStroke;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;

public interface InputHandler extends Closeable {
    void start();
    @NonNull KeyStroke readInput();

    @Nullable KeyStroke pollInput();
}
package io.github.bfur64.terminal.v3;

import io.github.bfur64.terminal.v3.pipeline.RenderType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TerminalConfig(RenderType renderType, int xSize, int ySize, boolean sizeOverride) {}

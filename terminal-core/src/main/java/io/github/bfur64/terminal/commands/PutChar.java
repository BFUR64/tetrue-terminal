package io.github.bfur64.terminal.commands;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record PutChar(int x, int y, char out) implements Command {}

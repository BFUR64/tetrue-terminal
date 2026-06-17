package io.github.bfur64.terminal.commands;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record SetBg(int r, int g, int b) implements  Command {}

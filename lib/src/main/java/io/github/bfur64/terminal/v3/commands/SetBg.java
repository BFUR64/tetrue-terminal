package io.github.bfur64.terminal.v3.commands;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record SetBg(int r, int g, int b) implements  Command {}

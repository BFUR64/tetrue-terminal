package io.github.bfur64.terminal.commands;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Put(int x, int y, String text) implements Command {}

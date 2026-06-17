package io.github.bfur64.terminal.commands;

import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record OnSGR(SGR SGR) implements Command {}

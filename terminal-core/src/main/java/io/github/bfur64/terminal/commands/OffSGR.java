package io.github.bfur64.terminal.commands;

import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record OffSGR(SGR SGR) implements Command {}

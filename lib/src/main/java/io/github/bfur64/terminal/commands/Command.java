package io.github.bfur64.terminal.commands;

import org.jspecify.annotations.NullMarked;

@NullMarked
public sealed interface Command permits Clear, Flush, Put, Reset, SetBg, SetFg {}

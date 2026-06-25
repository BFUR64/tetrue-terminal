package io.github.bfur64.terminal.render;

import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Set;

@NullMarked
public record Symbol(char character, @Nullable Color fg, @Nullable Color bg, Set<SGR> SGRs) {
    public Symbol {
        SGRs = Set.copyOf(SGRs);
    }
}

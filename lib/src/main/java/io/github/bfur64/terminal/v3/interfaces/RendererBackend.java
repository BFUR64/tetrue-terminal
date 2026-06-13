package io.github.bfur64.terminal.v3.interfaces;

import io.github.bfur64.terminal.v3.commands.Command;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RendererBackend {
    void execute(Command command);
}

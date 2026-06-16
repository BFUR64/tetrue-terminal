package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.commands.Command;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RendererBackend {
    void execute(Command command);
}

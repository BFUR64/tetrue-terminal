package io.github.bfur64.terminal.v3.pipeline;

import io.github.bfur64.terminal.v3.commands.Command;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface Pipeline {
    void execute(List<Command> commands);
}

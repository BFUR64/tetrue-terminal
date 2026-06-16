package io.github.bfur64.terminal.render;

import io.github.bfur64.terminal.commands.Command;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface RenderStrategy {
    void execute(List<Command> commands, int termXSize, int termYSize);
}

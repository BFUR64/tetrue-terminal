package io.github.bfur64.terminal.pipeline;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.commands.Command;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class ImmediateMode implements RenderStrategy {
    private final RendererBackend rendererBackend;

    public ImmediateMode(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;
    }

    @Override
    public void execute(List<Command> commands, int xSize, int ySize) {
        for (Command command : commands) {
            rendererBackend.execute(command);
        }
    }
}

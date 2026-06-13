package io.github.bfur64.terminal.v3.pipeline;

import io.github.bfur64.terminal.v3.interfaces.RendererBackend;
import io.github.bfur64.terminal.v3.commands.Command;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ImmediatePipeline implements Pipeline {
    private final RendererBackend rendererBackend;

    public ImmediatePipeline(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;
    }

    @Override
    public void execute(List<Command> commands) {
        for (Command command : commands) {
            rendererBackend.execute(command);
        }
    }
}

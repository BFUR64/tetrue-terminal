package io.github.bfur64.terminal.v3.pipeline;

import io.github.bfur64.terminal.v3.commands.Command;
import io.github.bfur64.terminal.v3.interfaces.RendererBackend;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class BufferedMode implements RenderMode {
    private final RendererBackend rendererBackend;

    public BufferedMode(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;
    }

    @Override
    public void execute(List<Command> commands, int xSize, int ySize) {

    }
}

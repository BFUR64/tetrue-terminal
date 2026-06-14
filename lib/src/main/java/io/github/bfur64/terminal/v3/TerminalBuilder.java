package io.github.bfur64.terminal.v3;

import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.jline.JLineRuntime;
import io.github.bfur64.terminal.v3.lanterna.LanternaRuntime;
import io.github.bfur64.terminal.v3.mock.MockRuntime;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public class TerminalBuilder {
    private @Nullable RuntimeType runtimeType;
    private @Nullable PipelineType pipelineType;
    private int xSize;
    private int ySize;
    private boolean sizeOverride;

    public TerminalBuilder auto() {
        return jline();
    }

    public TerminalBuilder jline() {
        this.runtimeType = RuntimeType.JLINE;
        return this;
    }

    public TerminalBuilder lanterna() {
        this.runtimeType = RuntimeType.LANTERNA;
        return this;
    }

    public TerminalBuilder mock() {
        this.runtimeType = RuntimeType.MOCK;
        return this;
    }

    public TerminalBuilder immediate() {
        this.pipelineType = PipelineType.IMMEDIATE;
        return this;
    }

    public TerminalBuilder buffered() {
        this.pipelineType = PipelineType.BUFFERED;
        return this;
    }

    public TerminalBuilder size(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.sizeOverride = true;
        return this;
    }

    public TerminalRuntime build() throws IOException {
        if (runtimeType == null) {
            throw new IllegalArgumentException("Runtime type must be either: auto(), jline(), lanterna(), or mock()");
        }

        if (pipelineType == null) {
            throw new IllegalArgumentException("Pipeline type must be either: immediate(), or buffered()");
        }

        TerminalConfig config = new TerminalConfig(pipelineType, xSize, ySize, sizeOverride);

        return switch (runtimeType) {
            case JLINE -> new JLineRuntime(config);
            case LANTERNA -> new LanternaRuntime(config);
            case MOCK -> new MockRuntime(config);
        };
    }
}

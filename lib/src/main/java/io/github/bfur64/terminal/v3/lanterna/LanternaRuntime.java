package io.github.bfur64.terminal.v3.lanterna;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.pipeline.ImmediatePipeline;
import io.github.bfur64.terminal.v3.pipeline.Pipeline;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LanternaRuntime implements TerminalRuntime {
    private final Terminal terminal;
    private final com.googlecode.lanterna.terminal.Terminal lanternaTerminal;

    public LanternaRuntime() {
        this.lanternaTerminal = new DefaultTerminalFactory().createTerminal();
        Pipeline pipeline = new ImmediatePipeline(new LanternaBackend(lanternaTerminal, lanternaTerminal.newTextGraphics()));

        this.terminal = new Terminal(pipeline, new LanternaInputSource(lanternaTerminal));

        lanternaTerminal.setCursorVisible(false);
        lanternaTerminal.enterPrivateMode();
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {
        lanternaTerminal.exitPrivateMode();
        lanternaTerminal.close();
    }

    @Override
    public int xSize() {
        return lanternaTerminal.getTerminalSize().getColumns();
    }

    @Override
    public int ySize() {
        return lanternaTerminal.getTerminalSize().getRows();
    }
}

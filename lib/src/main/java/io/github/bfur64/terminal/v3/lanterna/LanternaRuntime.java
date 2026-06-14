package io.github.bfur64.terminal.v3.lanterna;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.v3.pipeline.RenderType;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.TerminalConfig;
import io.github.bfur64.terminal.v3.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.pipeline.BufferedMode;
import io.github.bfur64.terminal.v3.pipeline.ImmediateMode;
import io.github.bfur64.terminal.v3.pipeline.RenderMode;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class LanternaRuntime implements TerminalRuntime, TerminalEnvironment {
    private static final int DEFAULT_X = 0;
    private static final int DEFAULT_Y = 0;

    private final Terminal terminal;
    private final com.googlecode.lanterna.terminal.Terminal lanternaTerminal;

    public LanternaRuntime(TerminalConfig config) throws IOException {
        if (config.sizeOverride()) {
            throw new UnsupportedOperationException("Lanterna does not support size override");
        }

        this.lanternaTerminal = new DefaultTerminalFactory().createTerminal();

        RenderMode renderMode = config.renderType() == RenderType.BUFFERED ?
                new BufferedMode(new LanternaBackend(lanternaTerminal, lanternaTerminal.newTextGraphics())) :
                new ImmediateMode(new LanternaBackend(lanternaTerminal, lanternaTerminal.newTextGraphics()));

        this.terminal = new Terminal(this, renderMode, new LanternaInputSource(lanternaTerminal));
    }

    private void start() {
        try {
            lanternaTerminal.enterPrivateMode();
            lanternaTerminal.setCursorVisible(false);
        }
        catch (IOException ignored) {}
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {
        try {
            lanternaTerminal.exitPrivateMode();
            lanternaTerminal.close();
        }
        catch (IOException ignored) {}
    }

    @Override
    public int xSize() {
        try {
            return lanternaTerminal.getTerminalSize().getColumns();
        }
        catch (IOException ignored) {
            return DEFAULT_X;
        }
    }

    @Override
    public int ySize() {
        try {
            return lanternaTerminal.getTerminalSize().getRows();
        }
        catch (IOException ignored) {
            return DEFAULT_Y;
        }
    }

    @Override
    public String terminalInfo() {
        return "Lanterna: " + Config.lanternaVersion;
    }
}

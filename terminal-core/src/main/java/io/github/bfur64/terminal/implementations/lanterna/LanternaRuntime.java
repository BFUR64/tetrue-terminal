package io.github.bfur64.terminal.implementations.lanterna;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.bfur64.Versions;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.render.*;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class LanternaRuntime implements TerminalRuntime, TerminalEnvironment {
    private static final Logger logger = LogManager.getLogger(LanternaRuntime.class);

    private boolean xSizeWarningIssued;
    private boolean ySizeWarningIssued;

    private static final int DEFAULT_X = 0;
    private static final int DEFAULT_Y = 0;

    private final Terminal terminal;
    private final Screen screen;
    private final com.googlecode.lanterna.terminal.Terminal lanternaTerminal;

    public LanternaRuntime() throws IOException {
        this.lanternaTerminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(lanternaTerminal);

        RendererBackend rendererBackend = new LanternaBackend(screen);

        this.terminal = new Terminal(this, new FrameBuilder(rendererBackend), new LanternaInputSource(lanternaTerminal));

        start();
    }

    private void start() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {
        try {
            screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
            screen.stopScreen();
        }
        catch (IOException e) {
            logger.error("Failed to close Lanterna runtime", e);
        }
    }

    @Override
    public int xSize() {
        try {
            return lanternaTerminal.getTerminalSize().getColumns();
        }
        catch (IOException e) {
            if (!xSizeWarningIssued) {
                xSizeWarningIssued = true;
                logger.warn("Could not get `xSize()`, defaulting to {}", DEFAULT_X, e);
            }
            return DEFAULT_X;
        }
    }

    @Override
    public int ySize() {
        try {
            return lanternaTerminal.getTerminalSize().getRows();
        }
        catch (IOException e) {
            if (!ySizeWarningIssued) {
                ySizeWarningIssued = true;
                logger.warn("Could not get `ySize()`, defaulting to {}", DEFAULT_Y, e);
            }
            return DEFAULT_Y;
        }
    }

    @Override
    public String terminalInfo() {
        return "Lanterna: " + Versions.LANTERNA;
    }
}

package io.github.bfur64.terminal.implementations.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.render.Symbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@NullMarked
public final class LanternaBackend implements RendererBackend {
    private static final Logger logger = LogManager.getLogger(LanternaBackend.class);
    private boolean screenFailed;

    private final Screen screen;
    private final TextGraphics textGraphics;

    public LanternaBackend(Screen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
    }

    @Override
    public void execute(Symbol[][] frame, int termXSize, int termYSize) {
        if (termXSize <= 0 || termYSize <= 0) return;

        screen.clear();

        for (int y = 0; y < termYSize; y++) {
            for (int x = 0; x < termXSize; x++) {
                Symbol symbol = frame[y][x];

                if (symbol == null) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.clearModifiers();
                    textGraphics.setCharacter(x, y, ' ');
                    continue;
                };

                textGraphics.setBackgroundColor(
                    symbol.bg() != null
                        ? new TextColor.RGB(symbol.bg().r(), symbol.bg().g(), symbol.bg().b())
                        : TextColor.ANSI.DEFAULT
                );

                textGraphics.setForegroundColor(
                    symbol.fg() != null
                        ? new TextColor.RGB(symbol.fg().r(), symbol.fg().g(), symbol.fg().b())
                        : TextColor.ANSI.DEFAULT
                );

                textGraphics.clearModifiers();
                if (!symbol.SGRs().isEmpty()) {
                    textGraphics.setModifiers(convertSGR(symbol.SGRs()));
                }

                textGraphics.setCharacter(x, y, symbol.cell());
            }
        }

        try {
            screen.refresh(Screen.RefreshType.DELTA);
        }
        catch (IOException e) {
            if (!screenFailed) {
                screenFailed = true;
                logger.error("Lanterna backend failed", e);
            }
        }
    }

    private EnumSet<com.googlecode.lanterna.SGR> convertSGR(Set<SGR> SGRs) {
        Set<com.googlecode.lanterna.SGR> localEnums = new HashSet<>();

        for (SGR sgr : SGRs) {
            localEnums.add(switch (sgr) {
                case BOLD -> com.googlecode.lanterna.SGR.BOLD;
                case REVERSE -> com.googlecode.lanterna.SGR.REVERSE;
                case UNDERLINE -> com.googlecode.lanterna.SGR.UNDERLINE;
                case ITALIC -> com.googlecode.lanterna.SGR.ITALIC;
                case STRIKETHROUGH -> com.googlecode.lanterna.SGR.CROSSED_OUT;
            });
        }

        return EnumSet.copyOf(localEnums);
    }
}

package io.github.bfur64.terminal.implementations.lanterna;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.render.Symbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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

    private @Nullable TextColor prevBg = null;
    private @Nullable TextColor prevFg = null;
    private @Nullable Set<SGR> prevSGRs = null;

    public LanternaBackend(Screen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
    }

    @Override
    public void draw(Symbol[][] frame, int termXSize, int termYSize) {
        if (termXSize <= 0 || termYSize <= 0) return;

        screen.clear();

        for (int y = 0; y < termYSize; y++) {
            for (int x = 0; x < termXSize; x++) {
                Symbol symbol = frame[y][x];

                if (symbol == null) continue;

                TextColor bg = symbol.bg() != null
                    ? new TextColor.RGB(symbol.bg().r(), symbol.bg().g(), symbol.bg().b())
                    : TextColor.ANSI.DEFAULT;

                if (!bg.equals(prevBg)) {
                    textGraphics.setBackgroundColor(bg);
                    prevBg = bg;
                }

                TextColor fg = symbol.fg() != null
                    ? new TextColor.RGB(symbol.fg().r(), symbol.fg().g(), symbol.fg().b())
                    : TextColor.ANSI.DEFAULT;

                if (!fg.equals(prevFg)) {
                    textGraphics.setForegroundColor(fg);
                    prevFg = fg;
                }

                if (!symbol.SGRs().equals(prevSGRs)) {
                    textGraphics.clearModifiers();
                    if (!symbol.SGRs().isEmpty()) {
                        textGraphics.setModifiers(convertSGR(symbol.SGRs()));
                    }
                    prevSGRs = symbol.SGRs();
                }

                textGraphics.setCharacter(x, y, symbol.character());
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
        EnumSet<com.googlecode.lanterna.SGR> result = EnumSet.noneOf(com.googlecode.lanterna.SGR.class);

        for (SGR sgr : SGRs) {
            result.add(switch (sgr) {
                case BOLD -> com.googlecode.lanterna.SGR.BOLD;
                case REVERSE -> com.googlecode.lanterna.SGR.REVERSE;
                case UNDERLINE -> com.googlecode.lanterna.SGR.UNDERLINE;
                case ITALIC -> com.googlecode.lanterna.SGR.ITALIC;
                case STRIKETHROUGH -> com.googlecode.lanterna.SGR.CROSSED_OUT;
            });
        }

        return result;
    }
}

package io.github.bfur64.terminal.implementations.jline;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.render.Symbol;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Display;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class JLineBackend implements RendererBackend {
    private final Display display;

    private int displayXSize;
    private int displayYSize;

    public JLineBackend(Terminal terminal) {
        this.display = new Display(terminal, true);
    }

    @Override
    public void execute(Symbol[][] frame, int termXSize, int termYSize) {
        if (termXSize <= 0 || termYSize <= 0) return;

        if (displayXSize != termXSize || displayYSize != termYSize) {
            display.reset();
            display.resize(Size.of(termXSize, termYSize));
            displayXSize = termXSize;
            displayYSize = termYSize;
        }

        List<AttributedString> lines = new ArrayList<>();
        for (int y = 0; y < termYSize; y++) {
            AttributedStringBuilder stringBuilder = new AttributedStringBuilder(termXSize);

            for (int x = 0; x < termXSize; x++) {
                Symbol symbol = frame[y][x];

                if (symbol == null) {
                    stringBuilder.style(AttributedStyle.DEFAULT);
                    stringBuilder.append(" ");
                    continue;
                }

                stringBuilder.style(buildStyle(symbol));
                stringBuilder.append(symbol.character());
            }

            lines.add(stringBuilder.toAttributedString());
        }

        display.update(lines, 0);
    }

    private AttributedStyle buildStyle(Symbol symbol) {
        AttributedStyle style = AttributedStyle.DEFAULT;

        if (symbol.bg() != null) {
            style = style.background(symbol.bg().r(), symbol.bg().g(), symbol.bg().b());
        }
        if (symbol.fg() != null) {
            style = style.foreground(symbol.fg().r(), symbol.fg().g(), symbol.fg().b());
        }

        for (SGR sgr : symbol.SGRs()) {
            style = switch (sgr) {
                case BOLD -> style.bold();
                case REVERSE -> style.inverse();
                case UNDERLINE -> style.underline();
                case ITALIC -> style.italic();
                case STRIKETHROUGH -> style.crossedOut();
            };
        }

        return style;
    }
}

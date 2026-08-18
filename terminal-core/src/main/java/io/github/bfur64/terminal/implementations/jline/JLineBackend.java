package io.github.bfur64.terminal.implementations.jline;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.render.Frame;
import io.github.bfur64.terminal.render.Symbol;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Display;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class JLineBackend implements RendererBackend {
    private final Display display;
    private final Frame prevFrame = new Frame();

    private @Nullable List<AttributedString> prevLines = new ArrayList<>();

    private int displayXSize;
    private int displayYSize;

    public JLineBackend(Terminal terminal) {
        this.display = new Display(terminal, true);
    }

    @Override
    public void draw(Frame frame, int termXSize, int termYSize) {
        if (termXSize <= 0 || termYSize <= 0) return;

        if (displayXSize != termXSize || displayYSize != termYSize) {
            display.reset();
            display.resize(Size.of(termXSize, termYSize));
            displayXSize = termXSize;
            displayYSize = termYSize;
            prevLines = null;
            prevFrame.resizeBuffer(0, 0);
        }

        List<AttributedString> newLines = new ArrayList<>(displayYSize);
        if (prevLines == null) {
            for (int y = 0; y < displayYSize; y++) {
                newLines.add(buildLine(frame, y));
            }
        }
        else {
            for (int y = 0; y < displayYSize; y++) {
                if (prevFrame.rowChanged(frame, y)) {
                    newLines.add(buildLine(frame, y));
                }
                else {
                    newLines.add(prevLines.get(y));
                }
            }
        }

        prevLines = newLines;
        prevFrame.copyFromFrame(frame);
        display.update(newLines, 0);
    }

    private AttributedString buildLine(Frame frame, int y) {
        AttributedStringBuilder builder = new AttributedStringBuilder(displayXSize);

        for (int x = 0; x < frame.getBufferXSize(); x++) {
            Symbol symbol = frame.getSymbol(x, y);

            if (symbol == null) {
                builder.style(AttributedStyle.DEFAULT);
                builder.append(" ");
                continue;
            }

            builder.style(buildStyle(symbol));
            builder.append(symbol.character());
        }

        return builder.toAttributedString();
    }

    private AttributedStyle buildStyle(Symbol symbol) {
        AttributedStyle style = AttributedStyle.DEFAULT;

        Color symbolBg = symbol.bg();
        if (symbolBg != null) {
            style = style.background(symbolBg.r(), symbolBg.g(), symbolBg.b());
        }

        Color symbolFg = symbol.fg();
        if (symbolFg != null) {
            style = style.foreground(symbolFg.r(), symbolFg.g(), symbolFg.b());
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

package io.github.bfur64.terminal.impl.jline;

import io.github.bfur64.terminal.Symbol;
import io.github.bfur64.terminal.SymbolBuffer;
import io.github.bfur64.terminal.interfaces.RendererHandler;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.*;
import org.jline.utils.InfoCmp.Capability;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@NullMarked
class JlineRendererHandler implements RendererHandler {

    private final PrintWriter printWriter;
    private final Terminal terminal;
    private final Display display;

    public JlineRendererHandler(Terminal terminal, PrintWriter printWriter) {
        this.terminal = terminal;
        this.printWriter = printWriter;

        display = new Display(terminal, true);
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    @Override
    public void start() {
        terminal.enterRawMode();
        terminal.puts(Capability.cursor_invisible);
        terminal.puts(Capability.enter_ca_mode);

        display.resize(terminal.getSize());

        terminal.flush();

    }

    @Override
    public void clearScreen() {
        display.clear();
        terminal.puts(Capability.clear_screen);
    }

    @Override
    public void put(int x, int y, String out) {
        terminal.puts(Capability.cursor_address, y, x);
        printWriter.print(out);
    }

    @Override
    public void flush() {
        printWriter.flush();
    }

    @Override
    public void setForegroundColor(int r, int g, int b) {
        printWriter.print(String.format("\u001b[38;2;%s;%s;%sm", r, g, b));
    }

    @Override
    public void setBackgroundColor(int r, int g, int b) {
        printWriter.print(String.format("\u001b[48;2;%s;%s;%sm", r, g, b));
    }

    @Override
    public void resetColorAndStyle() {
        printWriter.print("\u001b[0m");
        flush();
    }

    @Override
    public io.github.bfur64.terminal.Size getSize() {
        return io.github.bfur64.terminal.Size.of(display.getRows(), display.getColumns());
    }

    @Override
    public int getHeight() {
        return display.getRows();
    }

    @Override
    public int getWidth() {
        return display.getColumns();
    }

    @Override
    public void close() throws IOException {
        terminal.puts(Capability.cursor_visible);
        terminal.puts(Capability.exit_ca_mode);
        terminal.flush();

        terminal.close();
    }

    @Override
    public void flushBuffer(final SymbolBuffer buffer) {
        final int bufferHeight = buffer.getHeight();
        final int bufferWidth = buffer.getWidth();

        if (bufferHeight <= 0 || bufferWidth <= 0) return;

        display.resize(Size.of(bufferWidth, bufferHeight));

        final List<AttributedString> lines = new ArrayList<>();
        for (int y = 0; y < bufferHeight; y++) {
            final AttributedStringBuilder stringBuilder = new AttributedStringBuilder(bufferWidth);

            for (int x = 0; x < bufferWidth; x++) {
                final Symbol symbol = buffer.get(x, y);

                if (symbol == null) {
                    stringBuilder.append(" ", AttributedStyle.DEFAULT);
                    continue;
                }

                AttributedStyle style = AttributedStyle.DEFAULT
                        .backgroundRgb(symbol.backgroundColor().getRgb())
                        .foregroundRgb(symbol.foregroundColor().getRgb());

                stringBuilder.style(style);
                stringBuilder.append(symbol.value());
            }

            lines.add(stringBuilder.toAttributedString());
        }

        display.update(lines, 0);
        terminal.flush();
    }
}

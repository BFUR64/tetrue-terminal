package io.github.bfur64.terminal.v3;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.v3.commands.*;
import io.github.bfur64.terminal.v3.interfaces.InputSource;
import io.github.bfur64.terminal.v3.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.v3.pipeline.RenderMode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NullMarked
public final class Terminal {
    private final TerminalEnvironment environment;
    private final RenderMode renderMode;
    private final InputSource inputSource;

    private final List<Command> buffer = new ArrayList<>();

    public Terminal(TerminalEnvironment environment, RenderMode renderMode, InputSource inputSource) {
        this.environment = environment;
        this.renderMode = renderMode;
        this.inputSource = inputSource;
    }

    public static TerminalBuilder builder() {
        return new TerminalBuilder();
    }

    public KeyStroke read() {
        return inputSource.read();
    }

    public @Nullable KeyStroke poll() {
        return inputSource.poll();
    }

    public void setFg(TextColor color) {
        setFg(color.color());
    }

    public void setFg(Color color) {
        setFg(color.r(), color.g(), color.b());
    }

    public void setFg(int r, int g, int b) {
        buffer.add(new SetFg(r, g, b));
    }

    public void setBg(TextColor color) {
        setBg(color.color());
    }

    public void setBg(Color color) {
        setBg(color.r(), color.g(), color.b());
    }

    public void setBg(int r, int g, int b) {
        buffer.add(new SetBg(r, g, b));
    }

    public void put(int x, int y, char out) {
        put(x, y, String.valueOf(out));
    }

    public void put(int x, int y, String out) {
        buffer.add(new Put(x, y, out));
    }

    public void clear() {
        buffer.add(new Clear());
    }

    public void flush() {
        renderMode.execute(buffer, environment.xSize(), environment.ySize());
        buffer.clear();
    }

    public void reset() {
        buffer.add(new Reset());
    }

    public int xSize() {
        return environment.xSize();
    }

    public int ySize() {
        return environment.ySize();
    }

    public String terminalInfo() {
        return environment.terminalInfo();
    }

    public List<Command> snapshotBuffer() {
        return Collections.unmodifiableList(buffer);
    }
}

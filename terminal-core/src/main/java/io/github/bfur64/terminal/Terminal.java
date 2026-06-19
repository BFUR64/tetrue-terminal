package io.github.bfur64.terminal;

import io.github.bfur64.Versions;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.implementations.jline.JLineRuntime;
import io.github.bfur64.terminal.implementations.lanterna.LanternaRuntime;
import io.github.bfur64.terminal.implementations.mock.MockRuntime;
import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.output.TextColor;
import io.github.bfur64.terminal.interfaces.InputSource;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.render.FrameBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NullMarked
public final class Terminal {
    private static final Logger logger = LogManager.getLogger(Terminal.class);

    private final TerminalEnvironment environment;
    private final FrameBuilder frameBuilder;
    private final InputSource inputSource;

    private final List<Command> buffer = new ArrayList<>();

    public Terminal(TerminalEnvironment environment, FrameBuilder frameBuilder, InputSource inputSource) {
        this.environment = environment;
        this.frameBuilder = frameBuilder;
        this.inputSource = inputSource;
    }

    public static Builder builder() {
        return new Builder();
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

    public void onSGR(SGR SGR) {
        buffer.add(new OnSGR(SGR));
    }

    public void onSGR(SGR... SGRs) {
        for (SGR SGR : SGRs) {
            buffer.add(new OnSGR(SGR));
        }
    }

    public void onSGR(List<SGR> SGRs) {
        for (SGR SGR : SGRs) {
            buffer.add(new OnSGR(SGR));
        }
    }

    public void offSGR(SGR SGR) {
        buffer.add(new OffSGR(SGR));
    }

    public void offSGR(SGR... SGRs) {
        for (SGR SGR : SGRs) {
            buffer.add(new OffSGR(SGR));
        }
    }

    public void offSGR(List<SGR> SGRs) {
        for (SGR SGR : SGRs) {
            buffer.add(new OffSGR(SGR));
        }
    }

    public void clear() {
        buffer.add(new Clear());
    }

    public void flush() {
        buffer.add(new Flush());
        frameBuilder.execute(buffer, environment.xSize(), environment.ySize());
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

    public String libraryInfo() {
        return Versions.TETRUE_TERMINAL;
    }

    public String terminalInfo() {
        return environment.terminalInfo();
    }

    public void addToBuffer(Command command) {
        buffer.add(command);
    }

    public void addToBuffer(Command... commands) {
        buffer.addAll(Arrays.asList(commands));
    }

    public void addToBuffer(List<Command> commands) {
        buffer.addAll(new ArrayList<>(commands));
    }

    public List<Command> snapshotBuffer() {
        return Collections.unmodifiableList(buffer);
    }

    @NullMarked
    public static class Builder {
        private RuntimeType runtimeType = RuntimeType.JLINE;

        public Builder auto() {
            if (isTermux()) {
                return lanterna();
            }
            else {
                return jline();
            }
        }

        private static boolean isTermux() {
            String prefix = System.getenv("PREFIX");

            return (prefix != null &&
                    prefix.contains("termux")) ||
                    System.getenv("TERMUX_VERSION") != null;
        }

        public Builder jline() {
            this.runtimeType = RuntimeType.JLINE;
            return this;
        }

        public Builder lanterna() {
            this.runtimeType = RuntimeType.LANTERNA;
            return this;
        }

        public Builder mock() {
            this.runtimeType = RuntimeType.MOCK;
            return this;
        }

        public TerminalRuntime build() throws IOException {
            TerminalRuntime terminalRuntime = switch (runtimeType) {
                case JLINE -> new JLineRuntime();
                case LANTERNA -> new LanternaRuntime();
                case MOCK -> new MockRuntime();
            };

            logger.info("Initialized {} runtime", runtimeType);

            return terminalRuntime;
        }
    }
}

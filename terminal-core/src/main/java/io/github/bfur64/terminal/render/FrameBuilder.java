package io.github.bfur64.terminal.render;

import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public final class FrameBuilder {
    private final RendererBackend rendererBackend;
    private final Frame frame = new Frame();

    private final Set<SGR> activeSGRs = new HashSet<>();
    private @Nullable Color frameFg;
    private @Nullable Color frameBg;

    public FrameBuilder(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;
    }

    public void render(List<Command> commands, int termXSize, int termYSize) {
        if (frame.getBufferXSize() != termXSize || frame.getBufferYSize() != termYSize) {
            frame.resizeBuffer(termXSize, termYSize);
        }

        int bufferXSize = frame.getBufferXSize();
        int bufferYSize = frame.getBufferYSize();

        for (Command command : commands) {
            switch (command) {
                case Clear ignored -> frame.newBuffer();
                case OffSGR(SGR sgr) -> activeSGRs.remove(sgr);
                case OnSGR(SGR sgr) -> activeSGRs.add(sgr);
                case Put(int x, int y, String text) -> {
                    char[] charArray = text.toCharArray();

                    for (int i = 0; i < charArray.length; i++) {
                        int px = x + i;

                        if (px >= 0 &&
                            y >= 0 &&
                            px < bufferXSize &&
                            y < bufferYSize
                        ) {
                            frame.setSymbol(px, y, new Symbol(charArray[i], frameFg, frameBg, activeSGRs));
                        }
                    }
                }
                case PutChar(int x, int y, char out) when (
                    x >= 0 &&
                    y >= 0 &&
                    x < bufferXSize &&
                    y < bufferYSize
                ) -> frame.setSymbol(x, y, new Symbol(out, frameFg, frameBg, activeSGRs));
                case PutChar ignored -> {}
                case Reset ignored -> {
                    activeSGRs.clear();
                    frameFg = null;
                    frameBg = null;
                }
                case SetBg(int r, int g, int b) -> frameBg = Color.of(r, g, b);
                case SetFg(int r, int g, int b) -> frameFg = Color.of(r, g, b);
            }
        }

        rendererBackend.draw(frame, termXSize, termYSize);
    }
}

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
                case OffSGR offSGR -> activeSGRs.remove(offSGR.SGR());
                case OnSGR onSGR -> activeSGRs.add(onSGR.SGR());
                case Put put -> {
                    char[] text = put.text().toCharArray();
                    int x = put.x();
                    int y = put.y();

                    for (int i = 0; i < text.length; i++) {
                        if (x + i < bufferXSize && y < bufferYSize) {
                            frame.setSymbol(x + i, y, new Symbol(text[i], frameFg, frameBg, activeSGRs));
                        }
                    }
                }
                case PutChar putChar -> {
                    int x = putChar.x();
                    int y = putChar.y();

                    if (x < bufferXSize && y < bufferYSize) {
                        frame.setSymbol(x, y, new Symbol(putChar.out(), frameFg, frameBg, activeSGRs));
                    }
                }
                case Reset ignored -> {
                    activeSGRs.clear();
                    frameFg = null;
                    frameBg = null;
                }
                case SetBg setBg -> frameBg = Color.of(setBg.r(), setBg.g(), setBg.b());
                case SetFg setFg -> frameFg = Color.of(setFg.r(), setFg.g(), setFg.b());
            }
        }

        rendererBackend.draw(frame, termXSize, termYSize);
    }
}

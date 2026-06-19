package io.github.bfur64.terminal.render;

import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public class FrameBuilder {
    private Symbol[][] frame;

    private int frameXSize = 0;
    private int frameYSize = 0;

    private final Set<SGR> activeSGRs = new HashSet<>();
    private @Nullable Color frameFg;
    private @Nullable Color frameBg;

    public FrameBuilder() {
        frame = new Symbol[frameXSize][frameYSize];
    }

    public void execute(List<Command> commands, int termXSize, int termYSize) {
        if (frameXSize != termXSize || frameYSize != termYSize) {
            frame = copyFrame(frame, frameXSize, frameYSize, termXSize, termYSize);
            frameXSize = termXSize;
            frameYSize = termYSize;
        }

        for (Command command : commands) {
            switch (command) {
                case Clear ignored -> frame = new Symbol[frameXSize][frameYSize];
                case Flush ignored -> {}
                case OffSGR offSGR -> activeSGRs.remove(offSGR.SGR());
                case OnSGR onSGR -> activeSGRs.add(onSGR.SGR());
                case Put put -> {
                    char[] text = put.text().toCharArray();
                    int x = put.x();
                    int y = put.y();

                    for (int i = 0; i < text.length; i++) {
                        if (x + i < frameXSize && y < frameYSize) {
                            frame[y][x + i] = new Symbol(text[i], frameFg, frameBg, Set.copyOf(activeSGRs));
                        }
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
    }

    private Symbol[][] copyFrame(Symbol[][] oldFrame, int oldXSize, int oldYSize, int newXSize, int newYSize) {
        Symbol[][] localFrame = new Symbol[newXSize][newYSize];

        for (int y = 0; y < newYSize; y++) {
            for (int x = 0; x < newXSize; x++) {
                if (y < oldYSize && x < oldXSize) {
                    localFrame[y][x] = oldFrame[y][x];
                }
            }
        }

        return localFrame;
    }
}

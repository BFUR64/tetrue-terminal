package io.github.bfur64.terminal.pipeline;

import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.Color;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public final class BufferedMode implements RenderStrategy {
    private static final Color DEFAULT_COLOR = Color.of(-1, -1, -1);
    private static final Set<SGR> DEFAULT_SGR = Set.of();
    private static final Cell DEFAULT_CELL = new Cell(' ', DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_SGR);

    private final RendererBackend rendererBackend;

    private int frameXSize = 0;
    private int frameYSize = 0;

    private Cell[][] prevBuffer;
    private Cell[][] nextBuffer;

    private Color frameFg = DEFAULT_COLOR;
    private Color frameBg = DEFAULT_COLOR;
    private final Set<SGR> frameSGRs = new HashSet<>();

    private Color termFg = DEFAULT_COLOR;
    private Color termBg = DEFAULT_COLOR;
    private final Set<SGR> termSGRs = new HashSet<>();

    private boolean fullRedraw;

    public BufferedMode(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;

        prevBuffer = createBuffer(frameXSize, frameYSize);
        nextBuffer = createBuffer(frameXSize, frameYSize);
    }

    @Override
    public void execute(List<Command> commands, int termXSize, int termYSize) {
        if (frameXSize != termXSize || frameYSize != termYSize) {
            prevBuffer = copyBuffer(prevBuffer, frameXSize, frameYSize, termXSize, termYSize);
            nextBuffer = copyBuffer(prevBuffer, frameXSize, frameYSize, termXSize, termYSize);

            frameXSize = termXSize;
            frameYSize = termYSize;
            fullRedraw = true;
        }

        for (Command command : commands) {
            switch (command) {
                case Clear ignored -> nextBuffer = createBuffer(frameXSize, frameYSize);
                case Flush ignored -> {
                    if (fullRedraw) {
                        rendererBackend.execute(new Reset());
                        rendererBackend.execute(new Clear());
                        termFg = DEFAULT_COLOR;
                        termBg = DEFAULT_COLOR;
                        termSGRs.clear();
                    }

                    for (int y = 0; y < frameYSize; y++) {
                        for (int x = 0; x < frameXSize; x++) {
                            Cell prev = prevBuffer[y][x];
                            Cell next = nextBuffer[y][x];

                            if (fullRedraw && !prev.equals(DEFAULT_CELL)) {
                                renderCell(prev, x, y);
                            }

                            if (!prev.equals(next)) {
                                renderCell(next, x, y);
                                prevBuffer[y][x] = next;
                            }
                        }
                    }

                    fullRedraw = false;
                    nextBuffer = createBuffer(frameXSize, frameYSize);
                    rendererBackend.execute(new Flush());
                }
                case OffSGR offSGR -> frameSGRs.remove(offSGR.SGR());
                case OnSGR onSGR -> frameSGRs.add(onSGR.SGR());
                case Put put -> {
                    char[] text = put.text().toCharArray();
                    int x = put.x();
                    int y = put.y();

                    for (int i = 0; i < text.length; i++) {
                        if (x + i < frameXSize && y < frameYSize) {
                            nextBuffer[y][x + i] = new Cell(text[i], frameFg, frameBg, Set.copyOf(frameSGRs));
                        }
                    }
                }
                case Reset ignored -> {
                    frameFg = DEFAULT_COLOR;
                    frameBg = DEFAULT_COLOR;
                    frameSGRs.clear();
                }
                case SetBg setBg -> frameBg = Color.of(setBg.r(), setBg.g(), setBg.b());
                case SetFg setFg -> frameFg = Color.of(setFg.r(), setFg.g(), setFg.b());
            }
        }
    }

    private void renderCell(Cell cell, int x, int y) {
        boolean fgDefault = cell.foreground().equals(DEFAULT_COLOR);
        boolean bgDefault = cell.background().equals(DEFAULT_COLOR);

        if ((fgDefault && !termFg.equals(DEFAULT_COLOR)) ||
            (bgDefault && !termBg.equals(DEFAULT_COLOR)) ||
            !cell.SGRs().equals(termSGRs))
        {
            rendererBackend.execute(new Reset());

            termFg = DEFAULT_COLOR;
            termBg = DEFAULT_COLOR;

            termSGRs.clear();

            for (SGR SGR : cell.SGRs()) {
                rendererBackend.execute(new OnSGR(SGR));
                termSGRs.add(SGR);
            }
        }

        if (!fgDefault && !cell.foreground().equals(termFg)) {
            termFg = cell.foreground();
            rendererBackend.execute(new SetFg(termFg.r(), termFg.g(), termFg.b()));
        }

        if (!bgDefault && !cell.background().equals(termBg)) {
            termBg = cell.background();
            rendererBackend.execute(new SetBg(termBg.r(), termBg.g(), termBg.b()));
        }

        rendererBackend.execute(new Put(x, y, String.valueOf(cell.character())));
    }

    private Cell[][] createBuffer(int xSize, int ySize) {
        Cell[][] localBuffer = new Cell[ySize][xSize];

        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                localBuffer[y][x] = DEFAULT_CELL;
            }
        }

        return localBuffer;
    }

    private Cell[][] copyBuffer(Cell[][] oldBuffer, int oldXSize, int oldYSize, int newXSize, int newYSize) {
        Cell[][] newBuffer = createBuffer(newXSize, newYSize);

        for (int y = 0; y < newYSize; y++) {
            for (int x = 0; x < newXSize; x++) {
                if(y < oldYSize && x < oldXSize) {
                    newBuffer[y][x] = oldBuffer[y][x];
                }
            }
        }

        return newBuffer;
    }

    @NullMarked
    public record Cell(char character, Color foreground, Color background, Set<SGR> SGRs) {}
}

package io.github.bfur64.terminal.pipeline;

import io.github.bfur64.terminal.commands.*;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.output.Color;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class BufferedMode implements RenderStrategy {
    private static final Color DEFAULT_COLOR = Color.of(-1, -1, -1);

    private final RendererBackend rendererBackend;

    private int xSize = 0;
    private int ySize = 0;

    private Cell[][] prevBuffer;
    private Cell[][] nextBuffer;

    private Color frameFg = DEFAULT_COLOR;
    private Color frameBg = DEFAULT_COLOR;

    private Color termFg = DEFAULT_COLOR;
    private Color termBg = DEFAULT_COLOR;

    private boolean fullRedraw;

    public BufferedMode(RendererBackend rendererBackend) {
        this.rendererBackend = rendererBackend;

        prevBuffer = createBuffer();
        nextBuffer = createBuffer();
    }

    @Override
    public void execute(List<Command> commands, int xSize, int ySize) {
        if (this.xSize != xSize || this.ySize != ySize) {
            this.xSize = xSize;
            this.ySize = ySize;

            prevBuffer = createBuffer();
            nextBuffer = createBuffer();
            fullRedraw = true;

            termFg = DEFAULT_COLOR;
            termBg = DEFAULT_COLOR;
        }

        for (Command command : commands) {
            switch (command) {
                case Clear ignored -> nextBuffer = createBuffer();
                case Flush ignored -> {
                    if (fullRedraw) {
                        rendererBackend.execute(new Clear());
                        fullRedraw = false;
                    }

                    for (int y = 0; y < this.ySize; y++) {
                        for (int x = 0; x < this.xSize; x++) {
                            Cell prev = prevBuffer[y][x];
                            Cell next = nextBuffer[y][x];

                            if (!prev.equals(next)) {
                                boolean fgDefault = next.foreground().equals(DEFAULT_COLOR);
                                boolean bgDefault = next.background().equals(DEFAULT_COLOR);

                                if ((fgDefault && !termFg.equals(DEFAULT_COLOR)) ||
                                    (bgDefault && !termBg.equals(DEFAULT_COLOR)))
                                {
                                    rendererBackend.execute(new Reset());

                                    termFg = DEFAULT_COLOR;
                                    termBg = DEFAULT_COLOR;
                                }

                                if (!fgDefault && !next.foreground().equals(termFg)) {
                                    termFg = next.foreground();
                                    rendererBackend.execute(new SetFg(termFg.r(), termFg.g(), termFg.b()));
                                }

                                if (!bgDefault && !next.background().equals(termBg)) {
                                    termBg = next.background();
                                    rendererBackend.execute(new SetBg(termBg.r(), termBg.g(), termBg.b()));
                                }

                                rendererBackend.execute(new Put(x, y, String.valueOf(next.character())));

                                prevBuffer[y][x] = next;
                            }
                        }
                    }

                    nextBuffer = createBuffer();
                    rendererBackend.execute(new Flush());
                }
                case Put put -> {
                    char[] text = put.text().toCharArray();
                    int x = put.x();
                    int y = put.y();

                    for (int i = 0; i < text.length; i++) {
                        if (x + i < xSize && y < ySize) {
                            nextBuffer[y][x + i] = new Cell(text[i], frameFg, frameBg);
                        }
                    }
                }
                case Reset ignored -> {
                    frameFg = DEFAULT_COLOR;
                    frameBg = DEFAULT_COLOR;
                }
                case SetBg setBg -> frameBg = Color.of(setBg.r(), setBg.g(), setBg.b());
                case SetFg setFg -> frameFg = Color.of(setFg.r(), setFg.g(), setFg.b());
            }
        }
    }

    private Cell[][] createBuffer() {
        Cell[][] localBuffer = new Cell[ySize][xSize];

        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                localBuffer[y][x] = new Cell(' ', DEFAULT_COLOR, DEFAULT_COLOR);
            }
        }

        return localBuffer;
    }

    @NullMarked
    public record Cell(
        char character,
        Color foreground,
        Color background
    ) {}
}

package io.github.bfur64.terminal;

import io.github.bfur64.terminal.commands.Command;
import io.github.bfur64.terminal.commands.Put;
import io.github.bfur64.terminal.commands.PutChar;
import io.github.bfur64.terminal.implementations.mock.MockBackend;
import io.github.bfur64.terminal.render.Frame;
import io.github.bfur64.terminal.render.FrameBuilder;
import io.github.bfur64.terminal.render.Symbol;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class FrameBuilderTest {
    private FrameBuilder frameBuilder;
    private int xSize;
    private int ySize;

    private static Symbol symbol(char ch) {
        return new Symbol(ch, null, null, new HashSet<>());
    }

    @Before public void setUp() {
        frameBuilder = new FrameBuilder(new MockBackend());
        xSize = ySize = 10;
    }

    @Test
    public void render_emptyCommands_leavesFrameBlank() {
        frameBuilder.render(List.of(), xSize, ySize);
        Frame frame = frameBuilder.getFrameCopy();

        for (int y = 0; y < frame.getBufferYSize(); y++) {
            for (int x = 0; x < frame.getBufferXSize(); x++) {
                assertNull(frame.getSymbol(x, y));
            }
        }
    }

    @Test public void put_singleCharacter_placesAtCorrectPosition() {
        List<Command> commands = List.of(new PutChar(0, 0, 'x'));

        frameBuilder.render(commands, xSize, ySize);
        Frame frame = frameBuilder.getFrameCopy();

        assertEquals(symbol('x'), frame.getSymbol(0, 0));
    }

    @Test public void put_multipleCharacters_placesAllInOrder() {
        List<Command> commands = List.of(
            new PutChar(0, 0, 'H'),
            new PutChar(1, 0, 'I')
        );

        frameBuilder.render(commands, xSize, ySize);
        Frame frame = frameBuilder.getFrameCopy();

        assertEquals(symbol('H'), frame.getSymbol(0, 0));
        assertEquals(symbol('I'), frame.getSymbol(1, 0));
    }

    @Test public void put_overlappingCommands_lastOneWins() {
        List<Command> commands = List.of(
                new PutChar(0, 0, 'a'),
                new PutChar(0, 0, 'b')
        );
        frameBuilder.render(commands, xSize, ySize);
        assertEquals(symbol('b'), frameBuilder.getFrameCopy().getSymbol(0, 0));
    }

    @Test public void put_charOutsideFrame_isIgnored() {
        List<Command> commands = List.of(new PutChar(xSize, ySize, 'x'));

        frameBuilder.render(commands, xSize, ySize);

        assertNull(frameBuilder.getFrameCopy().getSymbol(0, 0));
    }

    @Test public void put_textOutsideFrame_isClipped() {
        List<Command> commands = List.of(new Put(-3, 0, "Hello"));

        frameBuilder.render(commands, xSize, ySize);
        Frame frame = frameBuilder.getFrameCopy();

        assertEquals(symbol('l'), frame.getSymbol(0, 0));
        assertEquals(symbol('o'), frame.getSymbol(1, 0));
        assertNull(frame.getSymbol(2, 0));
        assertNull(frame.getSymbol(xSize - 1, 0));
        assertNull(frame.getSymbol(xSize - 2, 0));
    }

    @Test public void render_resizeSmallerThenLarger_clipsOutOfBoundsContent() {
        List<Command> commands = List.of(
            new Put(0, 0, "Hello"),
            new Put(0, 5, "Hi")
        );
        frameBuilder.render(commands, 10, 10);

        frameBuilder.render(List.of(), 4, 4);

        frameBuilder.render(List.of(), 10, 10);

        Frame frame = frameBuilder.getFrameCopy();

        // Check frame size
        assertEquals(10, frame.getBufferXSize());
        assertEquals(10, frame.getBufferYSize());

        // Check if "Hello" is clipped
        assertEquals(symbol('H'), frame.getSymbol(0, 0));
        assertEquals(symbol('e'), frame.getSymbol(1, 0));
        assertEquals(symbol('l'), frame.getSymbol(2, 0));
        assertEquals(symbol('l'), frame.getSymbol(3, 0));
        assertNull(frame.getSymbol(4, 0));

        // Check if "Hi" is clipped
        assertNull(frame.getSymbol(0, 5));
        assertNull(frame.getSymbol(1, 5));
    }
}

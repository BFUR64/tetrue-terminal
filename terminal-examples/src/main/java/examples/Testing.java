package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.output.SGR;
import io.github.bfur64.terminal.output.TextColor;

public class Testing {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            terminal.setBg(TextColor.BEIGE);
            terminal.setFg(TextColor.BLACK);
            terminal.put(1, 1, " Hello World! ");
            terminal.reset();

            terminal.put(1, 3, 'a');
            terminal.onSGR(SGR.BOLD);
            terminal.put(2, 3, 'a');
            terminal.reset();
            terminal.onSGR(SGR.UNDERLINE);
            terminal.put(3, 3, 'a');
            terminal.reset();
            terminal.onSGR(SGR.ITALIC);
            terminal.put(4, 3, 'a');
            terminal.reset();
            terminal.onSGR(SGR.STRIKETHROUGH);
            terminal.put(5, 3, 'a');
            terminal.reset();
            terminal.onSGR(SGR.REVERSE);
            terminal.put(6, 3, 'a');
            terminal.reset();

            terminal.onSGR(SGR.UNDERLINE);
            terminal.put(3, 5, "I am ");
            terminal.onSGR(SGR.BOLD);
            terminal.put(8, 5, "UNDERLINED");
            terminal.reset();

            terminal.flush();

            terminal.read();
        }
    }
}

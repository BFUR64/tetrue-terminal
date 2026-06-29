package examples;

import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.output.Style;
import io.github.bfur64.terminal.output.TextColor;

public class Test {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            terminal.put(0, 0, "An error happened!", Config.ERROR_STYLE);
            terminal.put(0, 3, "Oh no, another error happened!", Config.ERROR_STYLE);
            terminal.put(3, 5, "Success!", Config.SUCCESS_STYLE);
            terminal.flush();
            terminal.read();
        }
    }
}

class Config {
    public static final Style ERROR_STYLE = Style.DEFAULT.fg(TextColor.RED).bg(TextColor.WHITE).underline().bold();
    public static final Style SUCCESS_STYLE = Style.DEFAULT.fg(TextColor.GREEN).bg(TextColor.WHITE).bold();
}
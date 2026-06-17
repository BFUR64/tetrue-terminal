package io.github.bfur64.terminal.interfaces;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TerminalEnvironment {
    int xSize();
    int ySize();
    String terminalInfo();
}

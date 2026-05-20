package io.github.bfur64.terminal.utils;

public interface TerminalBackend extends RendererHandler, InputHandler {
    void start();
    String getTerminalInfo();
}

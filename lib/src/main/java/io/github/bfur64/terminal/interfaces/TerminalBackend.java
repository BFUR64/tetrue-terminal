package io.github.bfur64.terminal.interfaces;

public interface TerminalBackend extends RendererHandler, InputHandler {
    void start();
    String getTerminalInfo();
}

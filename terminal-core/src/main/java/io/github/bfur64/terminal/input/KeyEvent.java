package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record KeyEvent(Key key) implements InputEvent {
    @Override
    public String toString() {
        return switch (key) {
            case ESCAPE -> "Escape";
            case BACKSPACE -> "Backspace";
            case ENTER -> "Enter";
            case ARROW_UP -> "Arrow Up";
            case ARROW_DOWN -> "Arrow Down";
            case ARROW_LEFT -> "Arrow Left";
            case ARROW_RIGHT -> "Arrow Right";
            case HOME -> "Home";
            case END -> "End";
            case PAGE_UP -> "Page Up";
            case PAGE_DOWN -> "Page Down";
            case UNKNOWN -> "Unknown";
        };
    }
}

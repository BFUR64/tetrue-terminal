# Welcome to Tetrue Terminal

**Tetrue Terminal** is a Java 21 library for building opinionated, fullscreen terminal applications with automatic backend detection, modern text handling, and full keyboard support.

[Get Started](user-guide/getting-started.md){ .md-button }
[API Reference](api-reference.md){ .md-button }

---

## Features

- Fullscreen terminal UI
- Resource-safe runtime management
- Automatic backend selection (via `.auto()`)
- Simple drawing API
- Simple input handling
- Styling utility

## Quick Start

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.put(0, 0, "Hello World!", Style.DEFAULT.fg(TextColor.BLUE).bold().underline());
    terminal.flush();

    terminal.onSGR(SGR.BOLD);
    terminal.put(0, 1, "Press Any Key!");
    terminal.offSGR(SGR.BOLD);

    KeyStroke keyStroke = terminal.read();
    if (keyStroke.keyType() == KeyType.CHARACTER && keyStroke.character() == 't') {
        terminal.put(0, 2, "You pressed `t`!");
    }
    terminal.flush();

    terminal.read();
}
```

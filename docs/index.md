# Welcome to Tetrue Terminal

**Tetrue Terminal** is a Java 21 library for building opinionated, fullscreen terminal applications with automatic backend detection, modern text handling, and full keyboard support.

[Get Started](user-guide/getting-started.md){ .md-button }
[Advanced](advanced/introduction.md){ .md-button }
[Extras](extras/introduction.md){ .md-button }
[API Reference](api-reference.md){ .md-button }

---

## Features

- Build fullscreen terminal applications
- Automatic backend selection (via `.auto()`)
- Buffered rendering for flicker-free updates
- Blocking and non-blocking keyboard input
- Stateful and stateless styling
- Deterministic testing with a built-in Mock Terminal

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

---

## User guide reference

- [Getting started](user-guide/getting-started.md)
- [Managing lifecycle](user-guide/managing-lifecycle.md)
- [Drawing text](user-guide/drawing-text.md)
- [Reading input](user-guide/reading-input.md)
- [Color & Styling](user-guide/styling.md)
- [Advanced](advanced/introduction.md)
- [Extras](extras/introduction.md)

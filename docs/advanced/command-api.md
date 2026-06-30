# Command API

The previous page introduced `Command` through the Mock Terminal. This page explains what those commands are and how they can be used in tests.

`Command` is the internal representation of every drawing operation performed by the Terminal.

Every drawing operation performed by the `Terminal` is translated into one or more Command objects. For example, a styled `put()` expands into the color, attribute, drawing, and reset commands required to reproduce the requested output.

`Command` is a sealed interface with the following implementations:

- `Clear()`
- `OffSGR(SGR)`
- `OnSGR(SGR)`
- `Put(x, y, String)`
- `PutChar(x, y, char)`
- `Reset()`
- `SetBg(r, g, b)`
- `SetFg(r, g, b)`

!!! warning "Command API volatility"
    This API exists primarily to support testing and debugging. Although it is documented, it is considered internal and may change between patch or minor releases without notice.
    If you choose to depend on it directly, consult the `commands` package for the current set of supported commands.

## Testing with the Command API

You can use this in tandem with JUnit to confirm tests like the following:

Verifying if nothing happened:

```java
@Test public void noPrintTest() throws Exception {
    try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
        Terminal terminal = runtime.terminal();

        assertTrue(terminal.snapshotBuffer().isEmpty());
    }
}
```

Verifying a draw command:

```java
@Test public void simplePrintTest() throws Exception {
    try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
        Terminal terminal = runtime.terminal();

        terminal.put(0, 0, "Hello World!");

        assertEquals(new Put(0, 0, "Hello World!"), terminal.snapshotBuffer().getFirst());
    }
}
```

Verifying the color:

```java
@Test public void simpleColorTest() throws Exception {
    try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
        Terminal terminal = runtime.terminal();

        terminal.setFg(TextColor.RED);

        assertEquals(new SetFg(255, 0, 0), terminal.snapshotBuffer().getFirst());
    }
}
```

Verifying the order:

```java
@Test public void simpleOrderTest() throws Exception {
    try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
        Terminal terminal = runtime.terminal();

        terminal.clear();
        terminal.put(...);
        terminal.setFg(...);

        assertEquals(List.of(
            new Clear(),
            new Put(...),
            new SetFg(...)
        ), terminal.snapshotBuffer());
    }
}
```

Verifying the style:

```java
@Test public void simpleStyleTest() throws Exception {
    try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
        Terminal terminal = runtime.terminal();

        terminal.put(0, 0, "Hello World!", Style.DEFAULT.fg(TextColor.RED).underline());

        assertEquals(List.of(
            new SetFg(255, 0, 0),
            new OnSGR(SGR.UNDERLINE),
            new Put(0, 0, "Hello World!"),
            new Reset()
        ), terminal.snapshotBuffer());
    }
}
```

Because commands are ordinary Java objects with value-based equality, they can be compared directly using JUnit assertions. This avoids comparing ANSI escape sequences or screenshots, making tests deterministic across platforms and terminal implementations.

---

[Previous](mock-terminal.md){ .md-button }
[Introduction](introduction.md){ .md-button }

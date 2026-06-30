# Mock Terminal

The Mock Terminal is a backend that records draw commands instead of rendering them to a real terminal.

This is primarily useful for automated tests, where you want to verify what your application attempted to draw without opening a terminal window.

---

## Basic usage

Unlike `JLine` or `Lanterna`, the Mock Terminal does not display anything on screen. Instead, it stores every drawing command so your tests can inspect them later. The `Terminal` class provides the `snapshotBuffer()` method to make this possible.

```java
public class Main {
    public static void main(String[] args) throws Exception {
        List<Command> buffer;

        try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
            Terminal terminal = runtime.terminal();

            terminal.setFg(TextColor.RED);
            terminal.onSGR(SGR.UNDERLINE);
            terminal.put(0, 0, "Hello World!");

            // Returns an unmodifiable List
            buffer = new ArrayList<>(terminal.snapshotBuffer());

            // flush() consumes the buffered commands, so take a snapshot first
            terminal.flush();
        }

        // Inspect the recorded commands...
        System.out.println(buffer);
    }
}
```

Output:

```xml
[SetFg[r=255, g=0, b=0],

OnSGR[SGR=UNDERLINE],

Put[x=0, y=0, text=Hello World!]]
```

Most drawing operations, including `put()`, `clear()`, `setFg()`, `setBg()`, `onSGR()`, and `offSGR()`, are recorded as draw commands.

Rather than comparing ANSI escape sequences or screenshots, the Mock Terminal compares the library's internal drawing commands. This makes tests deterministic across platforms and terminal implementations.

---

## Advanced usage

The `TerminalRuntime` of the Mock Terminal is special. It exposes internal methods that you normally do not have access, such as `setXSize()`, `setYSize()`, and `addKeyStroke()`.

```java
try (MockRuntime runtime = (MockRuntime) Terminal.builder().mock().build()) {
    Terminal terminal = runtime.terminal();

    runtime.setXSize(30);
    runtime.setYSize(15);

    runtime.addKeyStroke(new KeyStroke(KeyType.UP));

    // Application logic proceeds as normal
}
```

!!! info "Why cast to MockRuntime?"
    The mock backend exposes testing utilities that do not exist on other backends, such as `snapshotBuffer()`, `addKeyStroke()`, and simulated terminal resizing. These are intentionally available only through `MockRuntime`.

These methods make it possible to simulate user input and terminal state during tests. Because the behavior is completely deterministic, applications can be tested without requiring a real terminal.

---

[Previous](backend-switching.md){ .md-button }
[Next](command-api.md){ .md-button }

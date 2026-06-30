# Managing Lifecycle

In this library, the `Terminal` and `TerminalRuntime` are separate ideas. One manipulates the Terminal, the other manages the lifecycle of the Terminal.

A `Terminal` contains user-facing methods for manipulating the Terminal (`put()`, `setBg()`, `flush()`) and getting envrinment information (`xSize()`, `ySize()`, `terminalInfo()`, `libraryInfo()`).

A `TerminalRuntime` manages the `TerminalBackend` and owns the `Terminal` reference. The runtime should be closed when no longer needed.

---

## Handling terminal lifecycle

There are two ways to close the runtime. The first is to call `runtime.close()` manually, and the second is to use a try‑with‑resources block that calls `close()` automatically.

!!! info "Running the code"
    The current section only illustrates the lifecycle. The next [section](#handling-exceptions) includes full examples and runnable code.

### Manually calling `close()`

```java
TerminalRuntime runtime = Terminal.builder().auto().build();
Terminal terminal = runtime.terminal();

terminal.put(0, 0, "Hello World!");
terminal.flush();

runtime.close();
```

### Wrapping in a try-with-resources

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.put(0, 0, "Hello World!");
    terminal.flush();
}
```

---

## Handling exceptions

`TerminalRuntime` implements `AutoCloseable` directly, so its `close()` method is allowed to throw a generic `Exception` (not just `IOException`). This is intentional because some backends may throw non‑IO exceptions during cleanup.

`Terminal.Builder`'s `build()` throws an `IOException` as a backend may fail to start properly.

You can handle it by catching the exception in a try-catch block, or by declaring `throws Exception` in the method signature.

### Try-catch handling

The Terminal always starts in full-screen alternate mode, so any `System.out.println()` may be sent to the alternate screen and immediately hidden when the runtime closes.

Using a logging framework that writes to a file or to stderr (which is often not redirected) will make sure you can see errors.

One commonly used logging framework is [Log4J2](https://logging.apache.org/log4j/2.x/){ target="_blank" }.

```java
public class Main {
    // Using Log4J2
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();
            terminal.put(0, 0, "Hello World!");
            terminal.flush();
            terminal.read();
        }
        // Catch the Exception and handle it via Log4J2
        catch (Exception e) {
            logger.error("Something really bad happened", e);
        }
    }
}
```

### Declaring `throws Exception` in the method signature

```java
public class Main {
    public static void main(String[] args) throws Exception { // Let it propagate upward
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();
            terminal.put(0, 0, "Hello World!");
            terminal.flush();
            terminal.read();
        }
    }
}
```

---

[Previous](getting-started.md){ .md-button }
[Next](drawing-text.md){ .md-button }

# Backend Switching

So far, every example has used `Terminal.builder().auto().build()`. For most applications, this is the recommended choice.

However, there are situations where you may want to select a backend explicitly, such as testing with a mock terminal or forcing a specific implementation.

The `Terminal.Builder` API allows switching between `Lanterna`, `JLine`, and `Mock` implementations.

The following examples show how you can use them.

```java
public class Main {
    public static void main(String[] args) throws Exception {
        // We use Lanterna instead of auto
        try (TerminalRuntime runtime = Terminal.builder().lanterna().build()) {
            Terminal terminal = runtime.terminal();
            // Application logic here
        }
    }
}
```

And if you want to switch backends via java args, we can do the following:

```java
public class App {
    private final Terminal terminal;

    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        // We create a private method that returns a runtime
        try (TerminalRuntime runtime = createRuntime(argsList)) {
            Terminal terminal = runtime.terminal();
            App app = new App(terminal);
            app.start();
        }
        catch (Exception error) {
            System.err.println("Terminal initialization failed: " + error.getMessage());
            System.exit(1);
        }
    }

    // If neither -jline nor -lanterna is specified, the builder remains unchanged and build() uses the default behavior (auto())
    private static TerminalRuntime createRuntime(List<String> args) throws IOException {
        Terminal.Builder builder = Terminal.builder().auto();

        if (args.contains("-jline")) {
            builder = builder.jline();
        } else if (args.contains("-lanterna")) {
            builder = builder.lanterna();
        }

        return builder.build();
    }
}
```

Explicit backend selection is primarily useful for testing, debugging, or environments where you already know which backend you want to use.

---

[Previous](introduction.md){ .md-button }
[Next](mock-terminals.md){ .md-button }

# Getting Started

**Tetrue Terminal** is a Java 21 library for building opinionated, fullscreen terminal applications.

!!! info "Java 21 required"
    Tetrue Terminal requires Java 21 or higher. Check in the Terminal with `java -version`. If you're using an older JDK, please upgrade before proceeding.

---

## Add Tetrue Terminal to your project

### Gradle (Kotlin DSL)

Add this to your `build.gradle.kts` `dependency { ... }` block:

``` kotlin
implementation("io.github.bfur64:tetrue-terminal:3.1.4")
```

### Maven

Add this to your `pom.xml` `<dependencies> ... </dependencies>` block:

``` xml
<dependency>
    <groupId>io.github.bfur64</groupId>
    <artifactId>tetrue-terminal</artifactId>
    <version>3.1.4</version>
</dependency>
```

---

## Your first Tetrue application

Create a main class:

``` java
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

public class Main {
    // TerminalRuntime.close() throws Exception
    public static void main(String[] args) throws Exception {
        // .auto() picks the best backend for your environment (JLine on desktop, Lanterna on Termux)
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            terminal.put(1, 1, "Hello Tetrue!");
            terminal.flush();

            // Waits for a key press before closing
            terminal.read();
        }

        // Once the program stops running, it will put you back to the original terminal screen
    }
}
```

Run the application from your terminal (e.g., Gradle `./gradlew run`). The terminal switches into fullscreen mode, displays `"Hello Tetrue!"`, waits for a key press, and then restores your original terminal session when it exits.

!!! warning "JLine on IDE"
    When using `.auto()` (which selects JLine by default), running inside an IDE may display a warning because the IDE’s built‑in terminal doesn’t support JLine’s full capabilities. For the best experience, run your application from a real terminal (e.g., Windows Terminal, iTerm2, GNOME Terminal).

---

[Next](managing-lifecycle.md){ .md-button }

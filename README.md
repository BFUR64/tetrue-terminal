[![License](https://img.shields.io/github/license/bfur64/tetrue-terminal)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.bfur64/tetrue-terminal)](https://central.sonatype.com/artifact/io.github.bfur64/tetrue-terminal)

<h1 align="center">Tetrue Terminal</h1>

<h3 align="center">Opinionated fullscreen terminal framework for Java 21+</h3>

<div align="center">
  <img width="864" height="456" alt="GIF showing colored bar moving with blinking text" src="https://github.com/user-attachments/assets/f1ddc8aa-b406-4bb8-abf4-86864b0f7e1c" />
</div>

---

<div align="center">
  <img width="864" height="456" alt="Colors, SGR, and text manipulation presented" src="https://github.com/user-attachments/assets/086095a3-fdbf-4609-800b-16105f6e53bd" />
</div>

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

## Features

- Build fullscreen terminal applications
- Automatic backend selection (via `.auto()`)
- Buffered rendering for flicker-free updates
- Blocking and non-blocking keyboard input
- Stateful and stateless styling
- Deterministic testing with a built-in Mock Terminal

## Why This Exists

Tetrue Terminal separates application code from terminal implementation details

This originally came from the concern of not being able to support Termux, as JLine3/4 did not work, while Lanterna did, and on Windows, Lanterna was clunky

Thus, this library was born to solve that issue. Since then, it has evolved into an opinionated framework with a consistent rendering and input model across multiple terminal backends.

## Installation / Running

### Kotlin

```kotlin
implementation("io.github.bfur64:tetrue-terminal:3.2.0")
```

### Maven

```xml
<dependency>
    <groupId>io.github.bfur64</groupId>
    <artifactId>tetrue-terminal</artifactId>
    <version>3.2.0</version>
</dependency>
```

## Wiki

Check out the [Wiki](https://bfur64.github.io/tetrue-terminal/)

## How It Works

Applications target the `Terminal` API rather than a specific terminal framework.

At runtime, Tetrue Terminal selects and manages the appropriate backend, allowing applications to switch implementations without changing application code.

## Requirements

- Java 21 or higher

### Tested Terminals

**Supported:**

- Windows Terminal (Windows 11)
- Powershell 7
- CMD.exe
- Linux xterm
- WSL2
- Termux (Android)

**Untested:**

- macOS Terminal, iTerm2
- Other Linux terminals

## Tech Stack

- **Build Tool**: Gradle 9.6.0
- **Language**: Java 21

## Contributing

Development takes place on the `dev` branch

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines and pull request workflow

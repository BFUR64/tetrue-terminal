[![License](https://img.shields.io/github/license/bfur64/menu-manager)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.bfur64/tetrue-terminal)](https://central.sonatype.com/artifact/io.github.bfur64/tetrue-terminal)

<h1 align="center">Tetrue Terminal</h1>

<h3 align="center">Opinionated terminal framework abstraction layer for Java 21+</h3>

<div align="center">
  <img width="612" height="296" alt="Tetrue Lite using Tetrue Terminal" src="https://github.com/user-attachments/assets/82459d5a-0a77-45b0-9c9e-877d924a65a9" />
</div>

<div align="center">
  <img width="612" alt="Colors, SGR, and text manipulation presented" src="https://github.com/user-attachments/assets/ebcf22f0-7015-43a6-980d-6b71124a8b8e" />
</div>

## Quick Start

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.setBg(TextColor.BLUE);
    terminal.onSGR(SGR.BOLD, SGR.UNDERLINE);
    terminal.put(0, 0, "Hello World!");
    terminal.reset();
    terminal.flush();

    KeyStroke keyStroke = terminal.read();
    if (keyStroke.keyType() == KeyType.CHARACTER && keyStroke.character() == 't') {
        terminal.put(0, 1, "You pressed `t`!");
    }
    terminal.flush();

    terminal.read();
}
```

## Installation / Running

### Kotlin

```kotlin
implementation("io.github.bfur64:tetrue-terminal:3.1.0")
```

### Maven

```xml
<dependency>
    <groupId>io.github.bfur64</groupId>
    <artifactId>tetrue-terminal</artifactId>
    <version>3.1.0</version>
</dependency>
```

## Features

- Terminal abstraction layer
- Automatic backend selection
- Text rendering
- Foreground / background colors
- Limited SGR support
- Keyboard input handling
- Resource-safe runtime management
- Backend-independent API

## Usage

### Reading Input

Reads a single key press from the terminal. This method blocks until input is available

It returns a `KeyStroke`, which represents a key that is pressed.

A `KeyStroke` can be printed with user friendly formatting via the `toString()` method call

It stores two fields:
- KeyType - The type of key pressed, including special keys
- @Nullable Character - If the `KeyType` is `CHARACTER`, the `char` field will be populated

```java
KeyStroke keyStroke = terminal.read();

if (keyStroke.keyType() == KeyType.ESCAPE) {
    terminal.put(0, 0, "You pressed " + keyStroke.keyType().toString()); // Prints the formatted `KeyType`
}
else if (keyStroke.keyType() == KeyType.CHARACTER) { // Checks if the type of key is a character
    if (keyStroke.character() == 'q') { // Prints the character key
        terminal.put(0, 0, "You pressed q");
    }
}

terminal.flush();
```

### Polling Input

Poll for input without blocking. Returns `null` if no input is available

```java
while (true) {
    KeyStroke keyStroke = terminal.poll();

    // Check if `keyStroke` is null first
    if (keyStroke == null) continue;

    if (keyStroke.keyType() == KeyType.ESCAPE) break;
}
```

It is recommended to use a refresh-capped while loop to prevent CPU waste, like `LockSupport`

```java
while (true) {
    long frameStart = System.nanoTime(); // Used for frame timing

    KeyStroke keyStroke = terminal.poll();

    String out = keyStroke == null
        ? "None"
        : keyStroke.toString();

    // Basic pattern for breaking out with polling
    if (keyStroke != null && keyStroke.keyType() == KeyType.ESCAPE) {
        break;
    }

    // Print the pressed key
    terminal.clear();
    terminal.put(0, 0, "You pressed: " + out);
    terminal.flush();

    // Lock the refresh at 60fps
    long deadline = frameStart + 1_000_000_000L / 60; // nsPerFrame
    long now = System.nanoTime();

    while (now < deadline) {
        LockSupport.parkNanos(deadline - now);
        now = System.nanoTime();
    }
}
```

### Writing Text

Write text or individual characters at a specific x and y terminal position

> [!IMPORTANT]
> Changes are buffered. Call `flush()` to render any pending `Terminal` commands.

```java
terminal.put(0, 0, "Hello World!");
terminal.put(0, 1, 'a');
terminal.flush();
```

### Colors and Styling

Apply styling attributes to output

```java
terminal.onSGR(SGR.BOLD);
terminal.put(0, 0, "Hello World!");
terminal.offSGR(SGR.BOLD);
terminal.flush();
```

Multiple styles can be enabled at once

```java
terminal.onSGR(SGR.UNDERLINE, SGR.BOLD);
terminal.put(0, 0, "Hello World!");
terminal.offSGR(SGR.UNDERLINE, SGR.BOLD);
terminal.flush();
```

Predefined colors via `TextColor`

```java
terminal.setBg(TextColor.MAROON);
terminal.put(0, 0, "Hello World!");
terminal.reset();
terminal.flush();
```

Manual RGB coloring

```java
terminal.setBg(255, 0, 0);
terminal.put(0, 0, "Hello World!");
terminal.reset();
terminal.flush();
```

### Managing Terminal State

Terminal state like colors and style can be reset to default via the following:

- `reset()` - Resets both color and sgr
- `offSGR()` Resets a specified sgr

```java
terminal.setBg(TextColor.BLUE);
terminal.onSGR(SGR.UNDERLINE);

terminal.put(0, 0, "Hello World!");

terminal.reset();
terminal.flush();
```

### Runtime Lifecycle

A `TerminalRuntime` manages the terminal backend and should be closed when no longer needed

Try-with resources can be used as it implements `AutoCloseable`

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.put(0, 0, "Hello World!");
    terminal.flush();
}

```

## API Reference

| Class | Description |
|-------|-------------|
| `TerminalRuntime` | Manages terminal backend lifecycle |
| `Terminal` | Main rendering and input API |
| `KeyStroke` | Represents user input |
| `TextColor` | Opinionated CSS1 colors |

## How It Works

Tetrue Terminal provides a unified API over multiple terminal implementations

Applications interact only with the `Terminal` API while the runtime selects and manages the appropriate backend implementation

This allows applications to switch terminal frameworks without modifying application code

## Requirements

- Java 21 or higher
- Relatively modern terminals

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
- **Build Tool**: Gradle 9.5.0
- **Language**: Java 21

## Why This Exists

Tetrue Terminal separates application code from terminal implementation details

This originally came from the concern of not being able to support Termux, as JLine3/4 did not work, while Lanterna did, and on Windows, Lanterna was clunky

Thus, this library was born to solve that issue. Since then, it has become opinionated on how a Terminal should work

## Contributing

Development takes place on the `dev` branch

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines and pull request workflow

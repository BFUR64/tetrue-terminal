[![License](https://img.shields.io/github/license/bfur64/menu-manager)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.bfur64/tetrue-terminal)](https://central.sonatype.com/artifact/io.github.bfur64/tetrue-terminal)

<h1 align="center">Tetrue Terminal</h1>

<h3 align="center">Opinionated terminal framework abstraction layer for Java 21+</h3>

<div align="center">
  <img width="612" height="296" alt="Tetrue Lite using Tetrue Terminal" src="https://github.com/user-attachments/assets/82459d5a-0a77-45b0-9c9e-877d924a65a9" />
</div>

## Quick Start

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.setBg(TextColor.BLUE);
    terminal.onSGR(SGR.BOLD, SGR.UNDERLINE);
    terminal.put(0, 0, "Hello World!");
    terminal.reset();

    KeyStroke keyStroke = terminal.read();
    if (keyStroke == KeyType.CHARACTER && keyStroke.character() == 't') {
        terminal.put(0, 1, "You pressed `t`!";
    }

    terminal.read();
}
```

## Installation / Running

#### Dependencies

```kotlin
implementation("io.github.bfur64:tetrue-terminal:x.x.x")
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

#### Reading

Reads a single key press from the terminal. This method blocks until input is available

```java
terminal.read();
```

#### Polling

Poll for input without blocking. Returns `null` if no input is available

```java
terminal.poll();
```

### Writing Text

Write text or individual characters at a specific x and y terminal position

```java
terminal.put(0, 0, "Hello World!");
terminal.put(0, 1, 'a');
```

### Colors and Styling

Apply styling attributes to output

```java
terminal.onSGR(SGR.BOLD);
terminal.put(0, 0, "Hello World!");
terminal.offSGR(SGR.BOLD);
```

Multiple styles can be enabled at once

```java
terminal.onSGR(SGR.UNDERLINE, SGR.BOLD);
terminal.put(0, 0, "Hello World!");
terminal.offSGR(SGR.UNDERLINE, SGR.BOLD);
```

Opinionated CSS1 coloring via `TextColor`

```java
terminal.setBg(TextColor.MAROON);
terminal.put(0, 0, "Hello World!");
terminal.reset();
```

Manual RGB coloring

```java
terminal.setBg(255, 0, 0);
terminal.put(0, 0, "Hello World!");
terminal.reset();
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
```

### Runtime Lifecycle

A `TerminalRuntime` manages the terminal backend and should be closed when no longer needed

Try-with resources can be used as it implements `AutoCloseable`

```java
try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
    Terminal terminal = runtime.terminal();

    terminal.put(0, 0, "Hello World!");
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
- Relatively modern terminals (Windows Terminal)

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

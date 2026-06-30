# Reading Input

The `Terminal` exposes two methods for reading user input: `read()` and `poll()`.

Both return a `KeyStroke`, which represents the key that was pressed.

---

## Blocking input

`read()` blocks until the user presses a key. Use it when your application cannot continue until input is received.

```java
while (true) {
    KeyStroke keyStroke = terminal.read(); // The application will pause here until a key press is detected

    if (keyStroke.keyType() == KeyType.ENTER) {
        terminal.put(0, 0, "Submitted!");
    }
}
```

---

## Non-blocking input

`poll()` returns immediately. If no key is available, it returns `null`. This is useful for applications such as games, where rendering should continue even if the user is not pressing any keys.

```java
loop:
while (true) {
    KeyStroke keyStroke = terminal.poll(); // Does not wait, and will immediately continue execution

    if (keyStroke != null) {
        switch (keyStroke.keyType()) {
            case UP -> moveUp();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            case ESCAPE -> { break loop; }
        }
    }
}
```

A tight polling loop will consume an entire CPU thread. In real applications, use a frame limiter or timing mechanism to cap the refresh rate. A real [capped polling](../extras/frame-timing.md) example can be seen in the extras page.

---

## KeyStroke

Polling and reading both return `KeyStroke`.

A `KeyStroke` represents the letter or special key that the user has pressed, excluding control keys (e.g. shift, ctrl, function, etc.).

A special key is a non-printable key such as `ENTER`, `ESCAPE`, or the arrow keys.

`KeyTyp`e identifies what kind of key was pressed. Some values represent printable characters (`CHARACTER`), while others represent special keys (`ENTER`, `ESCAPE`, `UP`, `DOWN`, and so on).

We want to read if the user pressed `ENTER` yet. To detect Enter, compare the returned KeyType against `KeyType.ENTER`.

```java
KeyStroke keyStroke = terminal.read();

if (keyStroke.keyType() == KeyType.ENTER) {
    terminal.put(0, 0, "You have pressed enter!");
}
else {
    terminal.put(0, 0, "That operation is not allowed.");
}

terminal.flush();
```

Let us say we want to get the letter 't' from the user. We must first check if the `KeyType` is a single `CHARACTER` or if it's a special key. When `keyType()` is `CHARACTER`, `character()` contains the typed character. However, its return type is `@Nullable Character`, so Java still requires a null check.

```java
KeyStroke keyStroke = terminal.read();

if (keyStroke.keyType() == KeyType.CHARACTER) {
    Character character = keyStroke.character();

    if (character != null && character == 't') {
        terminal.put(0, 0, "The user pressed the key we like!");
    }
    else {
        terminal.put(0, 0, "Try again...");
    }

    terminal.flush();
}
```

## Keystroke for polling

The only difference between `read()` and `poll()` `KeyStroke`s is that `poll()` can return `null`, as we do not block the Terminal for user input. We simply add a `null` check at every site where we want to check for user input to account for that.

```java
while (true) {
    KeyStroke keyStroke = terminal.poll();

    if (keyStroke != null && keyStroke.keyType() == KeyType.ENTER) {
        break;
    }

    terminal.clear();
    terminal.put(0, 0, "Size: " + (terminal.xSize() * terminal.ySize()));
    terminal.flush();
}
```

---

[Previous](drawing-text.md){ .md-button }
[Next](styling.md){ .md-button }

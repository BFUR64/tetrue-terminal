# Drawing Text

---

## Text coordinates

To print text, we must first understand how it is positioned. We use the `put()` method to print text to the terminal, which accepts an `(x, y)` position and either a String or a character.

Think of `(x, y)` as coordinates on the screen, similar to the coordinate system used in mathematics or many 2D game engines. `(0, 0)` always refers to the top-left corner of the terminal.

The diagram below shows the terminal coordinate system. `x` increases from left to right, while `y` increases from top to bottom.

```console
   0123456789...          x
  ┌──────────────────────┐
0 │                      │
1 │                      │
2 │                      │
  └──────────────────────┘
y
```

In this example, we are printing at `(0, 0)`.

```java
terminal.put(0, 0, "Hi!");
terminal.flush(); // "Hi!" will be printed to the top-left corner of the Terminal
```

Output:

```console
(0,0)
┌──────────────────────┐
│Hi!                   │
│                      │
│                      │
└──────────────────────┘
```

Let's offset the text by increasing the x coordinate, as we don't want to keep printing to the left side all the time. We can adjust the `x` position to achieve this.

In this example, we offset the text 5 characters to the right, but we still print at the very top of the Terminal.

```java
terminal.put(5, 0, "Hello!");
terminal.flush(); // This will be offset by 5 characters to the right
```

Output:

```console
(5, 0)
┌────────────────────┐
│     Hello!         │
│                    │
│                    │
└────────────────────┘
```

With this, we can now position text anywhere on the screen, so let us try printing "Hello World!" as two separate `put()` commands.

```java
terminal.put(1, 1, "Hello");
terminal.put(3, 1, "World!");
terminal.flush();
```

Output:

```console
(1, 1) and (3, 1)
┌────────────────────┐
│                    │
│ HeWorld!           │
│                    │
└────────────────────┘
```

That's not what we expected. "World!" overwrote part of "Hello" because both strings occupy the same row and overlap horizontally. If two `put()` calls overlap, the later one replaces the earlier characters.

Let's try again. Let's move the second string so the two no longer overlap.

```java
terminal.put(1, 1, "I have padding!");
terminal.put(3, 5, "I am in a random position!");
terminal.flush(); // This is now away from the corners of the Terminal
```

Output:

```console
(1, 1) and (3, 5)
┌──────────────────────────┐
│                          │
│ I have padding!          │
│                          │
│                          │
│                          │
│   I am in a random positi│
└──────────────────────────┘
```

Oh no! What is this? Our text is cut-off? This is because the Terminal has a set Size. We can query the Terminal size using `terminal.xSize()` and `terminal.ySize()`. Anything drawn outside those bounds is clipped.

---

## Printing text

Text can be written to the terminal one character at a time or as an entire `String`.

In this example, we print "hello" with the use of characters.

```java
terminal.put(0, 0, 'h');
terminal.put(1, 0, 'e');
terminal.put(2, 0, 'l');
terminal.put(3, 0, 'l');
terminal.put(4, 0, 'o');
terminal.flush();
```

To avoid repetitive `put()` calls, we can directly write Strings to achieve the same output.

```java
terminal.put(0, 0, "hello");
terminal.flush();
```

---

## Flushing to the Terminal

!!! info "Buffered Rendering"
    All drawing and state-changing operations are buffered in `Terminal`. Methods like `put()`, `setBg()`, `setFg()`, `onSGR()`, `offSGR()`, and `clear()` only record commands. Nothing is sent to the Terminal until `flush()` is called. Calling `flush()` submits all buffered commands at once.

In this example, we use two flushes to print to the Terminal. This shows that we can call `flush()` at any point in the application, and all the buffered commands are sent to the Terminal.

Because `flush()` only submits buffered commands, it does not erase anything that has already been rendered.

```java
terminal.put(0, 0, "Hello World!");
terminal.flush();
terminal.put(0, 3, "I am Tetrue!");
terminal.flush(); // "Hello World!" is still present after this flush
```

---

## Clearing the Terminal

The Terminal will never have its contents be cleared unless `clear()` is called. `clear()` removes everything currently displayed on the terminal.

`clear()` is buffered just like `put()`. It records a clear command, which is applied the next time `flush()` is called.

In this example, we do not call `flush()` after calling `clear()` to show it not taking effect.

```java
terminal.put(0, 0, "Hello World!");
terminal.flush();
terminal.clear(); // This will not do anything until flush is called
```

Output:

```console
┌────────────────────┐
│ Hello World!       │
│                    │
│                    │
└────────────────────┘
```

To make the clear operation take effect, call `flush()` after `clear()`.

```java
terminal.put(0, 0, "Hello World!");
terminal.flush();

terminal.clear();
terminal.flush(); // It will clear "Hello World!" and all previously written text
```

Output:

```console
┌────────────────────┐
│                    │
│                    │
│                    │
└────────────────────┘
```

Another way to clear text in the Terminal is by printing spaces. This allows you to erase specific portions of the screen instead of clearing the entire terminal.

For example, we can replace "World!" with spaces.

```java
terminal.put(0, 0, "Hello World!");
terminal.flush();

// We want to remove "World!", so we offset in the x position and print spaces
terminal.put(6, 0, "      "); // This replaces "World!" with spaces, blanking it from the Terminal
terminal.flush();
```

Output:

```console
┌────────────────────┐
│ Hello              │
│                    │
│                    │
└────────────────────┘
```

---

[Previous](managing-state.md){ .md-button }
[Next](reading-input.md){ .md-button }

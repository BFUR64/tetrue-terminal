# Managing Terminal State

`Terminal` records drawing commands, which are later interpreted by the backend's `FrameBuilder`. Because the builder maintains drawing state, styling commands such as `setFg()`, `setBg()`, `onSGR()`, and `offSGR()` continue to affect subsequent `put()` calls until `reset()` is called.

---

## Using stateful methods

In the following example, we apply a blue background with an underline. This style will be applied to every subsequent `put()` call until `reset()` is called.

```java
terminal.setBg(TextColor.BLUE);
terminal.onSGR(SGR.UNDERLINE);
terminal.put(0, 0, "Hello World!");
terminal.put(3, 3, "I am Tetrue!");
terminal.reset(); // `reset()` sets the color and sgr to default
terminal.flush();
```

---

## Statelss styling with Style

If you prefer not to modify the terminal's drawing state, use `Style`. A `Style` only affects the `put()` call it is passed to and does not affect subsequent `put()` calls.

Stateful styling is convenient when drawing many elements with the same appearance. `Style` is generally preferred for one-off styling or reusable theme definitions.

Because `Style` objects are immutable, they can be stored as constants, shared safely, and reused throughout your application.

In this example, we inline the `Style` directly into the `put()` method.

```java
terminal.put(0, 0, "Hello World!", Style.DEFAULT.bg(TextColor.BLUE).underline());
terminal.put(3, 3, "I should have default styles!");
// We no longer need to call `reset()`
terminal.flush();
```

You can also store reusable styles in a `Config` class. This is useful for styles that are used throughout your application, such as error or success messages.

```java
public class Config {
    public static final Style ERROR_STYLE = Style.DEFAULT.fg(TextColor.RED).bg(TextColor.WHITE).underline().bold();
    public static final Style SUCCESS_STYLE = Style.DEFAULT.fg(TextColor.GREEN).bg(TextColor.WHITE).bold();
}

public class Main {
    public static void main(String[] args) {
        terminal.put(0, 0, "An error happened!", Config.ERROR_STYLE);
        terminal.put(0, 3, "Oh no, another error happened!", Config.ERROR_STYLE);
        terminal.put(3, 5, "Success!", Config.SUCCESS_STYLE);
        terminal.flush();
    }
}
```

---

[Previous](managing-lifecycle.md){ .md-button }
[Next](drawing-text.md){ .md-button }

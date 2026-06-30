# Frame Timing

Using while loops in Java will lock the CPU thread at 100%. It's better to use a capped refresh rate to prevent CPU waste, such as `LockSupport`.

The following example shows that:

```java
while (true) {
    // We check when the frame started and record it for later
    long frameStart = System.nanoTime();

    KeyStroke keyStroke = terminal.poll();

    String out = keyStroke == null
        ? "None"
        : keyStroke.toString(); // toString() is overriden to output styled Strings

    // Basic pattern for breaking out with polling
    if (keyStroke != null && keyStroke.keyType() == KeyType.ESCAPE) {
        break;
    }

    // This is where our application logic and rendering usually runs
    terminal.clear();
    terminal.put(0, 0, "You pressed: " + out);
    terminal.flush();

    // Lock the refresh at 60 refresh cycles per second. You can change this
    // deadline refers to the hard limit of how much time we should sleep for
    long deadline = frameStart + 1_000_000_000L / 60; // nsPerFrame

    // now marks the end of the frame
    long now = System.nanoTime();

    // We sleep for half the time in this loop
    long remaining = (deadline - now) / 2;

    // Threshold of 1ns as parkNanos isn't very granular at that level, especially Windows
    if (remaining > 1_000_000) {
        // deadline - now represents the amount of time we should sleep for
        // Less than 0 usually means the application overran the deadline, like being late for rent
        // It usually means something taxing the CPU thread. This is the main cause for low fps
        LockSupport.parkNanos(deadline - now);
    }

    // Busy-wait for the remaining time. It's more efficient than a while (true) loop and more precise than parkNanos
    while (now < deadline) {
        Thread.onSpinWait();
        now = System.nanoTime();
    }
}
```

This hybrid approach sleeps while enough time remains, then switches to a short spin-wait for the final fraction of the frame. It reduces CPU usage while maintaining more consistent frame timing than sleeping alone.

---

[Introduction](introduction.md){ .md-button }

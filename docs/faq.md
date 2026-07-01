# Frequently Asked Questions

??? question "Why another Terminal library?"
    Existing terminal libraries are excellent building blocks, but they expose different APIs and capabilities. Tetrue Terminal provides a small, consistent API with deterministic rendering, buffered drawing, and built-in testing support for fullscreen terminal applications.

??? question "Why not use JLine or Lanterna directly?"
    Tetrue Terminal builds on top of JLine and Lanterna by providing a consistent rendering model, automatic backend selection, buffered drawing, styling, and deterministic testing across supported backends.

??? question "Is Tetrue Terminal a wrapper around JLine / Lanterna?"
    No. Tetrue Terminal uses JLine and Lanterna as rendering backends, but exposes its own rendering model, command representation, lifecycle management, styling system, and testing infrastructure. Applications target the Tetrue API rather than the underlying backend.

??? question "Why are there only a few drawing methods?"
    Most functionality is composed with a handful of primitives instead of a dozen of specialized methods.

??? question "Is rendering buffered? Or double-buffered?"
    The library uses a buffered approach, but the backends use double buffering where possible. Right now, with Lanterna and JLine, both use their respective double buffered approaches internally.

??? question "Why does the library have a Mock Terminal?"
    It allows users of the library to test and verify their drawing operations without needing a real Terminal.

??? question "Why is the Command API marked as unstable?"
    Commands are primarily an implementation detail exposed for testing.

??? question "Why doesn't the library have widgets?"
    Widgets are intentionally outside the scope of Tetrue Terminal. The library provides drawing primitives, styling, input handling, and rendering infrastructure. Higher-level UI components are better implemented as a separate library built on top of these foundations.

??? question "Why isn't there feature X?"
    New features are added when they reinforce the existing programming model, not simply because a backend supports them.

??? question "Can I write games with it?"
    Yes. A project called [Tetrue Lite](https://github.com/BFUR64/tetrue-lite) is the first consumer of this library.

??? question "Can I build TUIs instead of games?"
    Yes. Although originally developed with games in mind, the library is suitable for general fullscreen terminal applications. It is intentionally opinionated toward fullscreen alternate-screen mode.

??? question "Is Tetrue Terminal thread-safe?"
    No. Unless otherwise documented, Terminal operations should be performed from a single thread. If your application uses multiple threads, synchronize access yourself.

??? question "Does the library support Windows, Linux, and macOS?"
    Yes. Platform support depends on the selected backend. Using `.auto()` is recommended because it selects the most appropriate backend for the current environment.

??? question "Why Java 21?"
    Java 21 provides language features such as records, sealed interfaces, pattern matching, and improved switch expressions. These simplify the implementation and help keep the API and internal architecture maintainable.

??? question "Can I write my own backend?"
    No. The backend API is considered an internal implementation detail and is intentionally kept out of the public API.

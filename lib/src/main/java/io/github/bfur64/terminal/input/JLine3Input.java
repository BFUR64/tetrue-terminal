package io.github.bfur64.terminal.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JLine3Input implements TerminalInput {
    private final Terminal terminal;
    private final BindingReader bindingReader;
    private final KeyMap<KeyStroke> keyMap;

    public JLine3Input(Terminal terminal) {
        this.terminal = terminal;
        this.bindingReader = new BindingReader(terminal.reader());
        this.keyMap = buildKeyMap();
    }

    @Override
    public @NonNull KeyStroke readInput() {
        return bindingReader.readBinding(keyMap);
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        try {
            if (!terminal.reader().ready()) {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        keyMap.setAmbiguousTimeout(50);
        return bindingReader.readBinding(keyMap);
    }

    private KeyMap<KeyStroke> buildKeyMap() {
        KeyMap<KeyStroke> map = new KeyMap<>();

        map.bind(new KeyStroke(KeyType.ARROW_UP),    "\033[A");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN),  "\033[B");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033[C");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT),  "\033[D");

        map.bind(new KeyStroke(KeyType.ARROW_UP),    "\033OA");
        map.bind(new KeyStroke(KeyType.ARROW_DOWN),  "\033OB");
        map.bind(new KeyStroke(KeyType.ARROW_RIGHT), "\033OC");
        map.bind(new KeyStroke(KeyType.ARROW_LEFT),  "\033OD");

        map.bind(new KeyStroke(KeyType.BACKSPACE), "\177"); // DEL
        map.bind(new KeyStroke(KeyType.ENTER),     "\r");

        map.bind(new KeyStroke(KeyType.ESCAPE),    "\033");

        // All remaining letters and digits
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                map.bind(new KeyStroke(c), String.valueOf(c));
            }
        }

        map.setAmbiguousTimeout(10);

        return map;
    }
}

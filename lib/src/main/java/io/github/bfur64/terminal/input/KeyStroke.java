package io.github.bfur64.terminal.input;

public class KeyStroke {
    private final KeyType keyType;
    private final Character character;

    public KeyStroke(Character character) {
        this(KeyType.CHARACTER, character);
    }

    public KeyStroke(KeyType keyType) {
        this(keyType, null);
    }

    public KeyStroke(KeyType keyType, Character character) {
        this.keyType = keyType;
        this.character = character;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public Character getCharacter() {
        return character;
    }
}

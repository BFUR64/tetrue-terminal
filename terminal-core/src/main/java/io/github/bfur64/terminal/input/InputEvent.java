package io.github.bfur64.terminal.input;

public sealed interface InputEvent permits CharacterEvent, KeyEvent {}

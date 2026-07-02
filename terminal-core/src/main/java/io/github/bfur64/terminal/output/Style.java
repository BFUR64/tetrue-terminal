package io.github.bfur64.terminal.output;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public record Style(@Nullable Color fg, @Nullable Color bg, Set<SGR> sgrSet) {
    public static final Style DEFAULT = new Style(null, null, new LinkedHashSet<>());

    public Style {
        // Preserve insertion order while keeping the set immutable
        sgrSet = Collections.unmodifiableSet(new LinkedHashSet<>(sgrSet));
    }

    public Style fg(TextColor color) {
        return fg(color.color());
    }

    public Style fg(Color color) {
        return new Style(color, bg, sgrSet);
    }

    public Style fg(int r, int g, int b) {
        return new Style(Color.of(r, g, b), bg, sgrSet);
    }

    public Style bg(TextColor color) {
        return bg(color.color());
    }

    public Style bg(Color color) {
        return new Style(fg, color, sgrSet);
    }

    public Style bg(int r, int g, int b) {
        return new Style(fg, Color.of(r, g, b), sgrSet);
    }

    public Style sgr(SGR sgr) {
        return withSGRs(List.of(sgr));
    }

    public Style sgr(SGR... sgrList) {
        return withSGRs(Arrays.asList(sgrList));
    }

    public Style sgr(List<SGR> sgrList) {
        return withSGRs(sgrList);
    }

    public Style bold() {
        return sgr(SGR.BOLD);
    }

    public Style reverse() {
        return sgr(SGR.REVERSE);
    }

    public Style underline() {
        return sgr(SGR.UNDERLINE);
    }

    public Style italic() {
        return sgr(SGR.ITALIC);
    }

    public Style strikethrough() {
        return sgr(SGR.STRIKETHROUGH);
    }

    private Style withSGRs(Iterable<SGR> sgrs) {
        Set<SGR> copy = new LinkedHashSet<>(sgrSet);
        for (SGR sgr : sgrs) {
            copy.add(sgr);
        }
        return new Style(fg, bg, copy);
    }
}

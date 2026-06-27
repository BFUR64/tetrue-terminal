package io.github.bfur64.terminal.output;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public record Style(@Nullable Color fg, @Nullable Color bg, Set<SGR> sgrSet) {
    public Style {
        sgrSet = Set.copyOf(sgrSet);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Color fg;
        private @Nullable Color bg;
        private final Set<SGR> sgrSet = new HashSet<>();

        public Builder fg(TextColor color) {
            return fg(color.color());
        }

        public Style build() {
            return new Style(fg, bg, sgrSet);
        }

        public Builder fg(Color color) {
            fg = color;
            return this;
        }

        public Builder fg(int r, int g, int b) {
            fg = Color.of(r, g, b);
            return this;
        }

        public Builder bg(TextColor color) {
            return bg(color.color());
        }

        public Builder bg(Color color) {
            bg = color;
            return this;
        }

        public Builder bg(int r, int g, int b) {
            bg = Color.of(r, g, b);
            return this;
        }

        public Builder sgr(SGR sgr) {
            sgrSet.add(sgr);
            return this;
        }

        public Builder sgr(SGR ... sgrList) {
            sgrSet.addAll(Arrays.asList(sgrList));
            return this;
        }

        public Builder sgr(List<SGR> sgrList) {
            sgrSet.addAll(sgrList);
            return this;
        }
    }
}

package dev.moviestash;

public record MovieMatch(MovieResult movie, MatchType type) {

    public enum MatchType {
        EXACT,
        PARTIAL
    }
}

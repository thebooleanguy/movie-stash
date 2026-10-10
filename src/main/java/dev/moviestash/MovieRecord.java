package dev.moviestash;

public record MovieRecord(
		String fileName,
		int tmdbId,
		String title,
		int year,
		String releaseDate,
		String posterPath) {
}

package dev.moviestash;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.Locale;

public class MovieScanner {

	public List<Path> findVideos(Path path) throws IOException {
		try (Stream<Path> stream = Files.list(path)) {
			List<Path> searchResults = stream.filter(p -> Files.isRegularFile(p)
					&& p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".mkv"))
					.toList();

			return searchResults;
		}
	}

}

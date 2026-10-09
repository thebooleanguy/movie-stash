package dev.moviestash;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.Locale;

public class MovieScanner {

	private static final List<String> VIDEO_EXTENSIONS = List.of(".mp4", ".mkv", ".avi", ".mov");

	public List<Path> findVideos(Path path) throws IOException {
		try (Stream<Path> stream = Files.list(path)) {
			List<Path> searchResults = stream.filter(p -> Files.isRegularFile(p)
					&& checkExtension(p.getFileName().toString().toLowerCase(Locale.ROOT)))
					.toList();

			return searchResults;
		}
	}

	private boolean checkExtension(String filename) {
		for (String ext : VIDEO_EXTENSIONS) {
			if (filename.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

}

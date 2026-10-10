package dev.moviestash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SubtitleOrganizer {

	private final MovieFilenameParser parser = new MovieFilenameParser();

	public int organize(Path directory, List<MovieRecord> movies)
			throws IOException {

		List<Path> subtitles;

		try (var files = Files.list(directory)) {
			subtitles = files
					.filter(Files::isRegularFile)
					.filter(path -> path.getFileName().toString()
							.toLowerCase().endsWith(".srt"))
					.toList();
		}

		int moved = 0;

		for (Path subtitle : subtitles) {
			MovieCandidate candidate = parser.parse(subtitle);

			if (candidate == null) {
				System.out.println("Could not identify subtitle: "
						+ subtitle.getFileName());
				continue;
			}

			List<MovieRecord> exactMatches = movies.stream()
					.filter(movie -> movie.year() == candidate.getYear())
					.filter(movie -> normalize(movie.title())
							.equals(normalize(candidate.getTitle())))
					.toList();

			MovieRecord match = null;

			if (exactMatches.size() == 1) {
				match = exactMatches.get(0);
			} else if (exactMatches.isEmpty()) {
				List<MovieRecord> partialMatches = movies.stream()
						.filter(movie -> movie.year() == candidate.getYear())
						.filter(movie -> normalize(movie.title())
								.contains(normalize(candidate.getTitle())))
						.toList();

				// Only use a partial match when it is unambiguous.
				if (partialMatches.size() == 1) {
					match = partialMatches.get(0);
				}
			}

			if (match == null) {
				System.out.println("No unique movie match for subtitle: "
						+ subtitle.getFileName());
				continue;
			}

			Path recordedPath = Path.of(match.fileName());
			Path relativeFolder = recordedPath.getParent();

			Path movieFolder = relativeFolder != null
					? directory.resolve(relativeFolder)
					: directory.resolve(
							match.title() + " (" + match.year() + ")");

			if (!Files.isDirectory(movieFolder)) {
				System.out.println("Movie folder not found: " + movieFolder);
				continue;
			}

			String folderName = movieFolder.getFileName().toString();
			Path destination = movieFolder.resolve(folderName + ".srt");

			if (Files.exists(destination)) {
				System.out.println("Subtitle already exists; skipping: "
						+ destination.getFileName());
				continue;
			}

			Files.move(subtitle, destination);
			System.out.println("Moved subtitle: " + destination);
			moved++;
		}

		return moved;
	}

	private String normalize(String value) {
		return value.toLowerCase().replaceAll("[^a-z0-9]", "");
	}
}

package dev.moviestash;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MovieStash {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Enter a path to scan.");
			return;
		}

		Path path = Path.of(args[0]);

		MovieScanner scanner = new MovieScanner();
		MovieFilenameParser parser = new MovieFilenameParser();

		try {
			List<Path> searchResults = scanner.findVideos(path);
			for (Path videoPath : searchResults) {
				MovieCandidate candidate = parser.parse(videoPath);
				if (candidate == null) {
					System.out.println("Could not identify: " + videoPath.getFileName());
				} else {
					System.out.println(candidate.getTitle() + " (" + candidate.getYear() + ")");
				}
			}
		} catch (IOException ex) {
			System.out.println("Error reading path: " + ex.getMessage());
			return;
		}
	}
}

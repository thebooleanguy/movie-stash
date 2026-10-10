package dev.moviestash;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MovieStash {
	public static void main(String[] args) {

		try {
			MovieMetadataClient client = new MovieMetadataClient();

			MovieSearchResponse response = client.searchMovie("The Matrix", 1999);

			for (MovieResult movie : response.getResults()) {
				System.out.println(
						movie.getTitle() + " — " + movie.getReleaseDate());
			}

		} catch (IOException | InterruptedException ex) {
			System.out.println("Movie search failed: " + ex.getMessage());
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

package dev.moviestash;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class MovieStash {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: MovieStash <directory>");
			return;
		}

		Path path = Path.of(args[0]);

		MovieScanner scanner = new MovieScanner();
		MovieFilenameParser parser = new MovieFilenameParser();
		MovieMetadataClient client = new MovieMetadataClient();
		MovieMatcher matcher = new MovieMatcher();

		try (Scanner input = new Scanner(System.in)) {

			List<Path> videoFiles = scanner.findVideos(path);
			MovieCatalog catalog = new MovieCatalog(path);
			List<MovieRecord> movies = catalog.load();

			for (Path videoPath : videoFiles) {
				System.out.println("\nFile: " + videoPath.getFileName());

				MovieCandidate candidate = parser.parse(videoPath);

				if (candidate == null) {
					System.out.println("Could not identify title and year.");
					continue;
				}

				String fileName = videoPath.getFileName().toString();

				boolean alreadyCatalogued = movies.stream()
						.anyMatch(existing -> existing.fileName().equals(fileName));

				if (alreadyCatalogued) {
					System.out.println("Already in catalog. Skipping TMDB search.");
					continue;
				}

				System.out.println(
						"Searching for: " + candidate.getTitle()
								+ " (" + candidate.getYear() + ")");

				try {
					MovieSearchResponse response = client.searchMovie(
							candidate.getTitle(),
							candidate.getYear());

					MovieMatch match = matcher.findBestMatch(
							candidate,
							response.getResults());

					if (match == null) {
						System.out.println("No suitable match found.");
						continue;
					}

					MovieResult movie = match.movie();

					boolean accepted;

					if (match.type() == MovieMatch.MatchType.EXACT) {
						System.out.println(
								"Matched: " + movie.getTitle()
										+ " (" + movie.getReleaseDate() + ")");
						accepted = true;
					} else {
						System.out.println(
								"Suggested match: " + movie.getTitle()
										+ " (" + movie.getReleaseDate() + ")");

						System.out.print("Accept this match? (y/n): ");
						String answer = input.nextLine().trim();

						accepted = answer.equalsIgnoreCase("y")
								|| answer.equalsIgnoreCase("yes");

						if (accepted) {
							System.out.println("Match accepted.");
						} else {
							System.out.println("Match skipped.");
						}
					}

					if (accepted) {
						MovieRecord record = new MovieRecord(
								fileName,
								movie.getId(),
								movie.getTitle(),
								candidate.getYear(),
								movie.getReleaseDate(),
								null);

						// Replace an existing entry for this filename.
						movies.removeIf(existing -> existing.fileName().equals(fileName));

						movies.add(record);

						System.out.println("Added to catalog.");
					}

				} catch (IOException ex) {
					System.out.println(
							"Movie search failed: " + ex.getMessage());
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					System.out.println("Movie search was interrupted.");
					return;
				}
			}

			catalog.save(movies);
			System.out.println("Catalog saved: " + movies.size() + " movies.");

		} catch (IOException ex) {
			System.out.println("Error reading directory: " + ex.getMessage());
		}
	}
}

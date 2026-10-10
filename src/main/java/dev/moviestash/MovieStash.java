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

		Path path = Path.of(args[0]).toAbsolutePath().normalize();

		MovieScanner scanner = new MovieScanner();
		MovieFilenameParser parser = new MovieFilenameParser();
		MovieMetadataClient client = new MovieMetadataClient();
		MovieMatcher matcher = new MovieMatcher();
		MovieRenamer renamer = new MovieRenamer();
		PosterDownloader posterDownloader = new PosterDownloader();
		SubtitleOrganizer subtitleOrganizer = new SubtitleOrganizer();

		try (Scanner input = new Scanner(System.in)) {
			List<Path> videoFiles = scanner.findVideos(path);
			MovieCatalog catalog = new MovieCatalog(path);
			List<MovieRecord> movies = catalog.load();

			boolean interrupted = false;

			for (Path videoPath : videoFiles) {
				System.out.println("\nFile: " + videoPath.getFileName());

				MovieCandidate candidate = parser.parse(videoPath);

				if (candidate == null) {
					System.out.println("Could not identify title and year.");
					continue;
				}

				String originalFileName = videoPath.getFileName().toString();

				boolean alreadyCatalogued = movies.stream()
						.anyMatch(existing -> existing.fileName().equals(originalFileName));

				if (alreadyCatalogued) {
					System.out.println("Already in catalog. Skipping TMDB search.");
					continue;
				}

				System.out.println("Searching for: " + candidate.getTitle()
						+ " (" + candidate.getYear() + ")");

				MovieResult movie;

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

					movie = match.movie();

					if (match.type() == MovieMatch.MatchType.EXACT) {
						System.out.println("Matched: " + movie.getTitle()
								+ " (" + movie.getReleaseDate() + ")");
					} else {
						System.out.println("Suggested match: " + movie.getTitle()
								+ " (" + movie.getReleaseDate() + ")");
						System.out.print("Accept this match? (y/n): ");

						String answer = input.nextLine().trim();

						if (!answer.equalsIgnoreCase("y")
								&& !answer.equalsIgnoreCase("yes")) {
							System.out.println("Match skipped.");
							continue;
						}

						System.out.println("Match accepted.");
					}
				} catch (IOException ex) {
					System.out.println("Movie search failed: " + ex.getMessage());
					continue;
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					System.out.println("Movie search was interrupted.");
					break;
				}

				// Organize the movie only after a match has been accepted.
				Path organizedPath;

				try {
					organizedPath = renamer.organize(
							videoPath,
							movie.getTitle(),
							candidate.getYear());

					System.out.println("Organized movie: " + organizedPath);
				} catch (IOException ex) {
					System.out.println("Could not organize movie: "
							+ ex.getMessage());
					continue;
				}

				// A poster failure should not prevent cataloguing the movie.
				String localPosterPath = null;

				try {
					Path poster = posterDownloader.download(
							movie.getPosterPath(),
							organizedPath.getParent(),
							"poster.jpg");

					if (poster != null) {
						localPosterPath = path.relativize(poster).toString();
						System.out.println("Poster saved: " + poster);
					} else {
						System.out.println("No poster is available for this movie.");
					}
				} catch (IOException ex) {
					System.out.println("Poster download failed: "
							+ ex.getMessage());
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					interrupted = true;
					System.out.println(
							"Poster download was interrupted; saving movie details.");
				}

				String catalogFileName = path.relativize(organizedPath).toString();

				MovieRecord record = new MovieRecord(
						catalogFileName,
						movie.getId(),
						movie.getTitle(),
						candidate.getYear(),
						movie.getReleaseDate(),
						localPosterPath);

				movies.removeIf(existing -> existing.fileName().equals(catalogFileName));
				movies.add(record);

				System.out.println("Added to catalog.");

				if (interrupted) {
					break;
				}
			}

			try {
				int movedSubtitles = subtitleOrganizer.organize(path, movies);
				System.out.println("Moved " + movedSubtitles + " subtitle(s).");
			} catch (IOException ex) {
				System.out.println("Subtitle organization failed: " + ex.getMessage());
			}

			catalog.save(movies);
			System.out.println("Catalog saved: " + movies.size() + " movies.");

		} catch (IOException ex) {
			System.out.println("Error reading directory or saving catalog: "
					+ ex.getMessage());
		}
	}
}

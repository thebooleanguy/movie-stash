package dev.moviestash;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class PosterDownloader {

	private final HttpClient httpClient = HttpClient.newHttpClient();

	// Keep the original method for existing tests and callers.
	public Path download(String posterPath, Path outputDirectory, int tmdbId)
			throws IOException, InterruptedException {
		return download(posterPath, outputDirectory, tmdbId + ".jpg");
	}

	// Allow the caller to choose the output filename.
	public Path download(String posterPath, Path outputDirectory, String filename)
			throws IOException, InterruptedException {

		if (posterPath == null || posterPath.isBlank()) {
			return null;
		}

		Files.createDirectories(outputDirectory);

		String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(imageUrl))
				.GET()
				.build();

		HttpResponse<byte[]> response = httpClient.send(
				request,
				HttpResponse.BodyHandlers.ofByteArray());

		if (response.statusCode() != 200) {
			throw new IOException(
					"Poster download failed with HTTP status "
							+ response.statusCode());
		}

		Path outputPath = outputDirectory.resolve(filename);
		Files.write(outputPath, response.body());

		return outputPath;
	}
}

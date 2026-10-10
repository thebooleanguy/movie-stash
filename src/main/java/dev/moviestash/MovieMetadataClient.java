package dev.moviestash;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MovieMetadataClient {
	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public MovieSearchResponse searchMovie(String title, int year)
			throws IOException, InterruptedException {

		String token = System.getenv("TMDB_BEARER_TOKEN");

		if (token == null || token.isBlank()) {
			throw new IllegalStateException(
					"TMDB_BEARER_TOKEN environment variable is missing");
		}

		String encodedTitle = URLEncoder.encode(
				title, StandardCharsets.UTF_8);

		String url = "https://api.themoviedb.org/3/search/movie"
				+ "?query=" + encodedTitle
				+ "&year=" + year;

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("Authorization", "Bearer " + token)
				.header("accept", "application/json")
				.GET()
				.build();

		HttpResponse<String> response = httpClient.send(
				request,
				HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new IOException(
					"TMDB returned HTTP " + response.statusCode());
		}

		return objectMapper.readValue(response.body(), MovieSearchResponse.class);
	}
}

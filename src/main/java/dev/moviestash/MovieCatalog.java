package dev.moviestash;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MovieCatalog {

	private final Path catalogPath;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public MovieCatalog(Path directory) {
		this.catalogPath = directory.resolve("movie-stash-catalog.json");
	}

	public List<MovieRecord> load() throws IOException {
		if (Files.notExists(catalogPath)) {
			return new ArrayList<>();
		}

		return objectMapper.readValue(
				catalogPath.toFile(),
				new TypeReference<List<MovieRecord>>() {
				});
	}

	public void save(List<MovieRecord> movies) throws IOException {
		objectMapper.writerWithDefaultPrettyPrinter()
				.writeValue(catalogPath.toFile(), movies);
	}
}

package dev.moviestash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieCatalogTest {

	@TempDir
	Path tempDir;

	@Test
	void loadsEmptyCatalogWhenFileDoesNotExist() throws IOException {
		MovieCatalog catalog = new MovieCatalog(tempDir);

		List<MovieRecord> movies = catalog.load();

		assertTrue(movies.isEmpty());
	}

	@Test
	void savesAndReloadsMovieRecords() throws IOException {
		MovieCatalog catalog = new MovieCatalog(tempDir);

		MovieRecord original = new MovieRecord(
				"Interstellar.2014.mkv",
				157336,
				"Interstellar",
				2014,
				"2014-11-04",
				null);

		catalog.save(List.of(original));

		List<MovieRecord> loaded = catalog.load();

		assertEquals(1, loaded.size());

		MovieRecord result = loaded.get(0);

		assertEquals(original.fileName(), result.fileName());
		assertEquals(original.tmdbId(), result.tmdbId());
		assertEquals(original.title(), result.title());
		assertEquals(original.year(), result.year());
		assertEquals(original.releaseDate(), result.releaseDate());
	}
}

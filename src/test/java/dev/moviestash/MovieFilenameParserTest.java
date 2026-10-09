package dev.moviestash;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;

class MovieFilenameParserTest {

	@Test
	void parsesMovieTitleAndYear() {
		MovieFilenameParser parser = new MovieFilenameParser();

		MovieCandidate result = parser.parse(Path.of("The.Matrix.1999.1080p.mkv"));

		assertEquals("The Matrix", result.getTitle());
		assertEquals(1999, result.getYear());
	}

	@Test
	void parsesTitleWithParenthesesAndYear() {
		MovieFilenameParser parser = new MovieFilenameParser();

		MovieCandidate result = parser.parse(Path.of("Interstellar (2014) [1080p].mp4"));

		assertEquals("Interstellar", result.getTitle());
		assertEquals(2014, result.getYear());
	}

	@Test
	void returnsNullWhenYearIsMissing() {
		MovieFilenameParser parser = new MovieFilenameParser();

		MovieCandidate result = parser.parse(Path.of("Some.Movie.mkv"));

		assertNull(result);
	}
}

package dev.moviestash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtitleOrganizerTest {

	@TempDir
	Path tempDir;

	private final SubtitleOrganizer organizer = new SubtitleOrganizer();

	@Test
	void movesMatchingSubtitleIntoMovieFolder() throws IOException {
		Path movieFolder = tempDir.resolve("The Matrix (1999)");
		Files.createDirectory(movieFolder);

		Path subtitle = tempDir.resolve("The.Matrix.1999.1080p.srt");
		Files.writeString(subtitle, "subtitle content");

		MovieRecord movie = new MovieRecord(
				"The Matrix (1999)/The Matrix (1999).mkv",
				603,
				"The Matrix",
				1999,
				"1999-03-30",
				"The Matrix (1999)/poster.jpg");

		int moved = organizer.organize(tempDir, List.of(movie));

		Path expected = movieFolder.resolve("The Matrix (1999).srt");

		assertEquals(1, moved);
		assertFalse(Files.exists(subtitle));
		assertTrue(Files.exists(expected));
		assertEquals("subtitle content", Files.readString(expected));
	}

	@Test
	void leavesSubtitleWhenNoMovieMatches() throws IOException {
		Path subtitle = tempDir.resolve("Unknown.Movie.2005.srt");
		Files.writeString(subtitle, "subtitle content");

		MovieRecord movie = new MovieRecord(
				"The Matrix (1999)/The Matrix (1999).mkv",
				603,
				"The Matrix",
				1999,
				"1999-03-30",
				null);

		int moved = organizer.organize(tempDir, List.of(movie));

		assertEquals(0, moved);
		assertTrue(Files.exists(subtitle));
	}

	@Test
	void doesNotOverwriteExistingSubtitle() throws IOException {
		Path movieFolder = tempDir.resolve("The Matrix (1999)");
		Files.createDirectory(movieFolder);

		Path subtitle = tempDir.resolve("The.Matrix.1999.srt");
		Files.writeString(subtitle, "new subtitle");

		Path existingSubtitle = movieFolder.resolve("The Matrix (1999).srt");
		Files.writeString(existingSubtitle, "existing subtitle");

		MovieRecord movie = new MovieRecord(
				"The Matrix (1999)/The Matrix (1999).mkv",
				603,
				"The Matrix",
				1999,
				"1999-03-30",
				null);

		int moved = organizer.organize(tempDir, List.of(movie));

		assertEquals(0, moved);
		assertTrue(Files.exists(subtitle));
		assertEquals("new subtitle", Files.readString(subtitle));
		assertEquals("existing subtitle",
				Files.readString(existingSubtitle));
	}
}

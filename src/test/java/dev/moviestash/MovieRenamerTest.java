package dev.moviestash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MovieRenamerTest {

	@TempDir
	Path tempDir;

	private final MovieRenamer renamer = new MovieRenamer();

	@Test
	void organizesMovieIntoFolderAndPreservesExtension() throws IOException {
		Path original = tempDir.resolve("The.Matrix.1999.1080p.mkv");
		Files.writeString(original, "fake movie content");

		Path result = renamer.organize(original, "The Matrix", 1999);

		Path movieFolder = tempDir.resolve("The Matrix (1999)");
		Path expected = movieFolder.resolve("The Matrix (1999).mkv");

		assertEquals(expected, result);
		assertTrue(Files.isDirectory(movieFolder));
		assertTrue(Files.exists(expected));
		assertFalse(Files.exists(original));
		assertEquals("fake movie content", Files.readString(expected));
	}

	@Test
	void organizesMovieWithDifferentExtension() throws IOException {
		Path original = tempDir.resolve("Interstellar.2014.mp4");
		Files.writeString(original, "fake movie content");

		Path result = renamer.organize(original, "Interstellar", 2014);

		Path expected = tempDir
				.resolve("Interstellar (2014)")
				.resolve("Interstellar (2014).mp4");

		assertEquals(expected, result);
		assertTrue(Files.exists(expected));
		assertFalse(Files.exists(original));
	}

	@Test
	void refusesToOverwriteExistingMovieFolder() throws IOException {
		Path original = tempDir.resolve("Matrix.1999.mkv");
		Files.writeString(original, "original movie");

		Path existingFolder = tempDir.resolve("The Matrix (1999)");
		Files.createDirectory(existingFolder);

		Path existingFile = existingFolder.resolve("keep.txt");
		Files.writeString(existingFile, "do not overwrite");

		assertThrows(
				IOException.class,
				() -> renamer.organize(original, "The Matrix", 1999));

		assertTrue(Files.exists(original));
		assertEquals("original movie", Files.readString(original));
		assertEquals("do not overwrite", Files.readString(existingFile));
	}

	@Test
	void handlesFilenameWithoutExtension() throws IOException {
		Path original = tempDir.resolve("SomeMovie1999");
		Files.writeString(original, "fake movie content");

		Path result = renamer.organize(original, "Some Movie", 1999);

		Path expected = tempDir
				.resolve("Some Movie (1999)")
				.resolve("Some Movie (1999)");

		assertEquals(expected, result);
		assertTrue(Files.exists(expected));
	}
}

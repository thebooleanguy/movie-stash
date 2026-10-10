package dev.moviestash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MovieRenamer {

	public Path organize(Path originalPath, String title, int year)
			throws IOException {

		String folderName = title + " (" + year + ")";
		Path movieFolder = originalPath.resolveSibling(folderName);

		String originalFileName = originalPath.getFileName().toString();
		int extensionIndex = originalFileName.lastIndexOf('.');

		String extension = extensionIndex >= 0
				? originalFileName.substring(extensionIndex)
				: "";

		Path newPath = movieFolder.resolve(folderName + extension);

		if (Files.exists(movieFolder)) {
			throw new IOException(
					"Destination folder already exists: " + movieFolder);
		}

		Files.createDirectory(movieFolder);

		try {
			return Files.move(originalPath, newPath);
		} catch (IOException ex) {
			// Don't leave an empty folder behind if moving fails.
			Files.deleteIfExists(movieFolder);
			throw ex;
		}
	}
}

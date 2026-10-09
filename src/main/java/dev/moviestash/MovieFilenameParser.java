package dev.moviestash;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MovieFilenameParser {
	private static final Pattern pattern = Pattern.compile("(19|20)\\d{2}");

	public MovieCandidate parse(Path path) {
		String filename = path.getFileName().toString();
		Matcher matcher = pattern.matcher(filename);

		if (!matcher.find()) {
			return null;
		}

		int year = Integer.parseInt(matcher.group());
		String title = filename.substring(0, matcher.start()).replaceAll("\\p{Punct}", " ").trim();

		return new MovieCandidate(title, year);
	}
}

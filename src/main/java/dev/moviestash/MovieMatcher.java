package dev.moviestash;

import java.util.List;
import java.util.Locale;

public class MovieMatcher {

	public MovieResult findBestMatch(
			MovieCandidate candidate,
			List<MovieResult> results) {
		String expectedTitle = normalize(candidate.getTitle());

		// First pass: exact title and year match.
		for (MovieResult result : results) {
			if (result.getReleaseDate() == null
					|| result.getReleaseDate().isBlank()) {
				continue;
			}

			int resultYear = Integer.parseInt(
					result.getReleaseDate().substring(0, 4));

			if (normalize(result.getTitle()).equals(expectedTitle)
					&& resultYear == candidate.getYear()) {
				return result;
			}
		}

		// Second pass: partial title match with the correct year.
		for (MovieResult result : results) {
			if (result.getReleaseDate() == null
					|| result.getReleaseDate().isBlank()
					|| result.getTitle() == null) {
				continue;
			}

			int resultYear = Integer.parseInt(
					result.getReleaseDate().substring(0, 4));

			String resultTitle = normalize(result.getTitle());

			if (resultYear == candidate.getYear()
					&& (resultTitle.contains(expectedTitle)
							|| expectedTitle.contains(resultTitle))) {
				return result;
			}
		}

		return null;
	}

	private String normalize(String title) {
		return title.trim().toLowerCase(Locale.ROOT);
	}
}

package dev.moviestash;

import java.util.List;
import java.util.Locale;

public class MovieMatcher {

	public MovieMatch findBestMatch(
			MovieCandidate candidate,
			List<MovieResult> results) {
		String expectedTitle = normalize(candidate.getTitle());

		// Prefer exact title and year matches.
		for (MovieResult result : results) {
			if (hasMatchingYear(candidate, result)
					&& result.getTitle() != null
					&& normalize(result.getTitle()).equals(expectedTitle)) {
				return new MovieMatch(result, MovieMatch.MatchType.EXACT);
			}
		}

		// Otherwise, find a possible partial match.
		for (MovieResult result : results) {
			if (hasMatchingYear(candidate, result)
					&& result.getTitle() != null) {

				String resultTitle = normalize(result.getTitle());

				if (resultTitle.contains(expectedTitle)) {
					return new MovieMatch(result, MovieMatch.MatchType.PARTIAL);
				}
			}
		}

		return null;
	}

	private boolean hasMatchingYear(
			MovieCandidate candidate,
			MovieResult result) {
		if (result.getReleaseDate() == null
				|| !result.getReleaseDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
			return false;
		}

		int resultYear = Integer.parseInt(
				result.getReleaseDate().substring(0, 4));

		return resultYear == candidate.getYear();
	}

	private String normalize(String title) {
		return title.trim().toLowerCase(Locale.ROOT);
	}
}

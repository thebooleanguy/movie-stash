package dev.moviestash;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieMatcherTest {

	private final MovieMatcher matcher = new MovieMatcher();

	@Test
	void prefersExactTitleAndYearMatch() {
		MovieCandidate candidate = new MovieCandidate("The Matrix", 1999);

		MovieResult documentary = new MovieResult();
		documentary.setTitle("Making \"The Matrix\"");
		documentary.setReleaseDate("1999-09-21");

		MovieResult movie = new MovieResult();
		movie.setTitle("The Matrix");
		movie.setReleaseDate("1999-03-31");
		MovieMatch match = matcher.findBestMatch(
				candidate, List.of(documentary, movie));

		assertNotNull(match);
		assertSame(movie, match.movie());
		assertEquals(MovieMatch.MatchType.EXACT, match.type());
	}

	@Test
	void doesNotMatchWhenYearIsDifferent() {
		MovieCandidate candidate = new MovieCandidate("The Matrix", 1999);

		MovieResult remake = new MovieResult();
		remake.setTitle("The Matrix");
		remake.setReleaseDate("2021-01-01");

		MovieMatch match = matcher.findBestMatch(
				candidate, List.of(remake));

		assertNull(match);
	}

	@Test
	void returnsNullWhenThereAreNoResults() {
		MovieCandidate candidate = new MovieCandidate("The Matrix", 1999);

		MovieMatch match = matcher.findBestMatch(
				candidate, List.of());

		assertNull(match);
	}
}

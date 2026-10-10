package dev.moviestash;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieSearchResponse {

	private int page;
	private List<MovieResult> results;
	private int totalResults;

	public int getPage() {
		return page;
	}

	public List<MovieResult> getResults() {
		return results;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setResults(List<MovieResult> results) {
		this.results = results;
	}

	@JsonProperty("total_results")
	public void setTotalResults(int total_results) {
		this.totalResults = total_results;
	}
}

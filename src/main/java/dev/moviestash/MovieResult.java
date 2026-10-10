
package dev.moviestash;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResult {

	private int id;
	private String title;

	@JsonProperty("release_date")
	private String releaseDate;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("release_date")
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
}

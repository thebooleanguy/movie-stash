package dev.moviestash;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResult {

	private int id;
	private String title;
	private String posterPath;

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

	public String getPosterPath() {
		return posterPath;
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

	@JsonProperty("poster_path")
	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}
}

package dev.moviestash;

public class MovieCandidate {
	private final String title;
	private final int year;

	public MovieCandidate(String title, int year) {
		this.title = title;
		this.year = year;
	}

	public String getTitle() {
		return this.title;
	}

	public int getYear() {
		return this.year;
	}
}

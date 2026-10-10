# Movie Stash

**A Java CLI tool for organizing your local movie collection with metadata from TMDB.**

Movie Stash scans a directory of movie files, identifies titles and release years from filenames, finds matching metadata using The Movie Database (TMDB), and organizes accepted matches into individual folders with standardized filenames, posters, and matching subtitles.

## Features

- **Movie scanning** — discovers supported video files in a directory.
- **Filename parsing** — extracts movie titles and release years from filenames.
- **TMDB integration** — searches The Movie Database for movie metadata.
- **Movie matching** — compares parsed filenames against search results.
- **Match confirmation** — asks for confirmation when a suggested match is uncertain.
- **Folder organization** — creates a folder for each movie and renames the video file consistently.
- **Poster downloads** — downloads available movie posters as `poster.jpg`.
- **Subtitle organization** — moves matching `.srt` subtitle files into their corresponding movie folders.
- **JSON catalog** — stores movie metadata and local file paths for future use.

## Example

Before:

```text
Movies/
├── The.Matrix.1999.1080p.mkv
├── The.Matrix.1999.1080p.srt
└── Interstellar.2014.mp4
```

After organization:

```text
Movies/
├── The Matrix (1999)/
│   ├── The Matrix (1999).mkv
│   ├── The Matrix (1999).srt
│   └── poster.jpg
├── Interstellar (2014)/
│   ├── Interstellar (2014).mp4
│   └── poster.jpg
└── movie-stash-catalog.json
```

Posters are downloaded when artwork is available and the download succeeds. Matching subtitles are moved when a unique movie match can be identified.

## Requirements

- Java 21 or a compatible JDK
- Maven
- A TMDB account and API bearer token

## Configuration

Movie Stash reads the TMDB bearer token from the `TMDB_BEARER_TOKEN` environment variable.

For a temporary shell session:

```bash
export TMDB_BEARER_TOKEN="your_token_here"
```

Obtain your API credentials through [TMDB](https://www.themoviedb.org/settings/api).

**Security:** Never commit your bearer token, `.env` files containing secrets, or private movie-library data to version control.

## Build and test

Clone the repository and navigate into the project directory:

```bash
git clone https://github.com/YOUR_USERNAME/movie-stash.git
cd movie-stash
```

Run the test suite:

```bash
mvn clean test
```

## Usage

Set your `TMDB_BEARER_TOKEN`, then pass the directory containing your movie files to the application.

If the Maven Exec Plugin is configured:

```bash
mvn exec:java \
  -Dexec.mainClass="dev.moviestash.MovieStash" \
  -Dexec.args="/path/to/Movies"
```

Replace `/path/to/Movies` with the directory you want to organize.

If the Exec Plugin is not configured, run `dev.moviestash.MovieStash` from your IDE with the movie directory as the program argument.

## How it works

1. Scans the specified directory for supported video files.
2. Parses filenames to extract movie titles and release years.
3. Searches TMDB for matching metadata.
4. Accepts exact matches and asks the user to confirm uncertain suggestions.
5. Creates a movie folder and moves and renames the video file.
6. Downloads the movie poster when available.
7. Finds matching `.srt` subtitle files and moves them into the appropriate movie folders.
8. Saves movie metadata and local paths in a JSON catalog.

Subtitle files can also be organized on subsequent runs when their corresponding movies are already recorded in the catalog.

## Supported video formats

The scanner currently recognizes:

- `.mp4`
- `.mkv`
- `.avi`
- `.mov`

File extensions are matched without regard to case.

## Supported subtitles

- `.srt`

Subtitle matching uses the parsed movie title and release year. Ambiguous or unmatched subtitles are left in place rather than assigned to a potentially incorrect movie.

## Project structure

```text
src/
├── main/java/dev/moviestash/
│   ├── MovieStash.java
│   ├── MovieScanner.java
│   ├── MovieFilenameParser.java
│   ├── MovieMetadataClient.java
│   ├── MovieMatcher.java
│   ├── MovieResult.java
│   ├── MovieRecord.java
│   ├── MovieCatalog.java
│   ├── MovieRenamer.java
│   ├── PosterDownloader.java
│   └── SubtitleOrganizer.java
└── test/java/dev/moviestash/
```

## Safety notes

- **Back up your files first.** Movie Stash moves and renames movie files.
- Test on a small directory or copies of your files before processing your main collection.
- Existing destination folders are not overwritten by the organizer.
- Existing subtitle files are not overwritten by the subtitle organizer.
- Poster downloads may fail because of network issues or unavailable artwork.
- Movie matching depends on the filename and the metadata returned by TMDB.
- Keep your API credentials out of source control.

## Roadmap

- Improve recovery from failed poster downloads.
- Improve handling of ambiguous or missing metadata.
- Add more robust end-to-end tests.
- Support additional subtitle formats, such as `.ass` and `.ssa`.
- Improve filename parsing for unusual releases and alternate editions.

## Tech stack

- Java 21
- Maven
- JUnit 5
- Jackson
- Java `HttpClient`
- TMDB API

## Acknowledgements

Movie metadata and poster artwork are provided by [The Movie Database (TMDB)](https://www.themoviedb.org/).

This project uses the TMDB API but is not endorsed or certified by TMDB.

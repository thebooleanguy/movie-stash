package dev.moviestash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PosterDownloaderTest {

    @TempDir
    Path tempDir;

    @Test
    void downloadsPosterFromTmdb() throws IOException, InterruptedException {
        PosterDownloader downloader = new PosterDownloader();

        Path poster = downloader.download(
                "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                tempDir,
                157336
        );

        assertNotNull(poster);
        assertTrue(Files.exists(poster));
        assertTrue(Files.size(poster) > 0);
    }
}

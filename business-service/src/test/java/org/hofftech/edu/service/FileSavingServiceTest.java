package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

class FileSavingServiceTest {

    private final FileSavingService fileSavingService = new FileSavingService();

    @Test
    void shouldSavePackagesToFile() throws Exception {
        List<Map<String, Long>> packages = List.of(
                Map.of("package1", 2L),
                Map.of("package2", 1L)
        );

        File outputFile = Files.createTempFile("packages", ".txt").toFile();
        fileSavingService.savePackagesToFile(packages, outputFile.getAbsolutePath(), true);

        List<String> lines = Files.readAllLines(outputFile.toPath());

        Assertions.assertThat(lines)
                .isNotEmpty()
                .contains("package1 - 2 шт", "package2 - 1 шт");

        Files.deleteIfExists(outputFile.toPath());
    }

    @Test
    void shouldThrowExceptionForInvalidFilePath() {
        List<Map<String, Long>> packages = List.of(Map.of("package1", 1L));

        Assertions.assertThatThrownBy(() -> fileSavingService.savePackagesToFile(packages, "/invalid/path/file.txt", true))
                .isInstanceOf(Exception.class);
    }
}


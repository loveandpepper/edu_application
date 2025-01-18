package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.exception.InputFileException;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class FileReaderServiceTest {

    @Test
    void shouldReadAllLinesFromFile() throws Exception {
        Path testFile = Files.createTempFile("test", ".txt");
        Files.writeString(testFile, "line1\nline2\nline3");

        List<String> lines = FileReaderService.readAllLines(testFile);

        Assertions.assertThat(lines).isNotNull()
                .hasSize(3)
                .containsExactly("line1", "line2", "line3");

        Files.deleteIfExists(testFile);
    }

    @Test
    void shouldThrowExceptionForNonExistentFile() {
        Path nonExistentFile = Paths.get("nonexistent.txt");

        Assertions.assertThatThrownBy(() -> FileReaderService.readAllLines(nonExistentFile))
                .isInstanceOf(InputFileException.class)
                .hasMessageContaining("Файл не существует");
    }
}


package com.honeyai.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BackupServiceTest {

    @TempDir
    Path tempDir;

    private Path databasePath;
    private Path backupDirectory;
    private BackupService backupService;

    @BeforeEach
    void setUp() throws IOException {
        databasePath = tempDir.resolve("data/honeyai.db");
        backupDirectory = tempDir.resolve("backups");

        Files.createDirectories(databasePath.getParent());
        Files.writeString(databasePath, "SQLite test data");

        backupService = new BackupService(
                databasePath.toString(),
                backupDirectory.toString());
    }

    @Test
    void performBackup_shouldCreateBackupFile() {
        // When
        Path result = backupService.performBackup();

        // Then
        assertThat(result).isNotNull();
        assertThat(Files.exists(result)).isTrue();
        assertThat(result.getFileName().toString()).startsWith("honeyai-backup-");
        assertThat(result.getFileName().toString()).endsWith(".db");
    }

    @Test
    void performBackup_shouldCopyDatabaseContent() throws IOException {
        // When
        Path result = backupService.performBackup();

        // Then
        assertThat(result).isNotNull();
        String content = Files.readString(result);
        assertThat(content).isEqualTo("SQLite test data");
    }

    @Test
    void performBackup_shouldCreateBackupDirectory() {
        // Given - backup dir does not exist yet
        assertThat(Files.exists(backupDirectory)).isFalse();

        // When
        backupService.performBackup();

        // Then
        assertThat(Files.exists(backupDirectory)).isTrue();
        assertThat(Files.isDirectory(backupDirectory)).isTrue();
    }

    @Test
    void performBackup_shouldReturnNull_whenDatabaseMissing() throws IOException {
        // Given
        Files.delete(databasePath);

        // When
        Path result = backupService.performBackup();

        // Then
        assertThat(result).isNull();
    }

    @Test
    void performBackup_shouldGenerateTimestampedFilename() {
        // When
        Path result = backupService.performBackup();

        // Then
        assertThat(result).isNotNull();
        String name = result.getFileName().toString();
        // Format: honeyai-backup-YYYY-MM-DD-HHmmss.db
        assertThat(name).matches("honeyai-backup-\\d{4}-\\d{2}-\\d{2}-\\d{6}\\.db");
    }

    @Test
    void performBackup_shouldCreateMultipleBackups() {
        // When
        Path first = backupService.performBackup();
        Path second = backupService.performBackup();

        // Then
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        // Files may have same timestamp if run in same second, but both should exist
        assertThat(Files.exists(first)).isTrue();
        assertThat(Files.exists(second)).isTrue();
    }

    @Test
    void cleanupOldBackups_shouldDeleteFilesOlderThan30Days() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);

        Path oldBackup = backupDirectory.resolve("honeyai-backup-2024-01-01-020000.db");
        Files.writeString(oldBackup, "old");
        Files.setLastModifiedTime(oldBackup,
                FileTime.from(Instant.now().minus(31, ChronoUnit.DAYS)));

        Path recentBackup = backupDirectory.resolve("honeyai-backup-2025-12-01-020000.db");
        Files.writeString(recentBackup, "recent");
        Files.setLastModifiedTime(recentBackup,
                FileTime.from(Instant.now().minus(5, ChronoUnit.DAYS)));

        // When
        backupService.cleanupOldBackups();

        // Then
        assertThat(Files.exists(oldBackup)).isFalse();
        assertThat(Files.exists(recentBackup)).isTrue();
    }

    @Test
    void cleanupOldBackups_shouldNotDeleteNonBackupFiles() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);

        Path otherFile = backupDirectory.resolve("important-data.txt");
        Files.writeString(otherFile, "keep this");
        Files.setLastModifiedTime(otherFile,
                FileTime.from(Instant.now().minus(60, ChronoUnit.DAYS)));

        // When
        backupService.cleanupOldBackups();

        // Then
        assertThat(Files.exists(otherFile)).isTrue();
    }

    @Test
    void performBackup_shouldRunCleanupAfterBackup() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);

        Path oldBackup = backupDirectory.resolve("honeyai-backup-2023-01-01-020000.db");
        Files.writeString(oldBackup, "old");
        Files.setLastModifiedTime(oldBackup,
                FileTime.from(Instant.now().minus(60, ChronoUnit.DAYS)));

        // When
        backupService.performBackup();

        // Then - old backup should be cleaned up
        assertThat(Files.exists(oldBackup)).isFalse();
    }
}

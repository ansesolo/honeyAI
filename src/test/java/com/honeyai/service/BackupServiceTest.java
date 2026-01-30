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

import com.honeyai.dto.BackupFileDto;

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

    // --- listRecentBackups tests ---

    @Test
    void listRecentBackups_shouldReturnBackupsSortedByDateDescending() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);
        Path older = backupDirectory.resolve("honeyai-backup-2026-01-01-020000.db");
        Path newer = backupDirectory.resolve("honeyai-backup-2026-01-15-020000.db");
        Files.writeString(older, "old");
        Files.writeString(newer, "new");
        Files.setLastModifiedTime(older,
                FileTime.from(Instant.now().minus(15, ChronoUnit.DAYS)));
        Files.setLastModifiedTime(newer,
                FileTime.from(Instant.now().minus(1, ChronoUnit.DAYS)));

        // When
        var result = backupService.listRecentBackups();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFilename()).contains("01-15");
        assertThat(result.get(1).getFilename()).contains("01-01");
    }

    @Test
    void listRecentBackups_shouldReturnEmptyList_whenNoBackupsExist() {
        // Given - backup directory doesn't exist yet

        // When
        var result = backupService.listRecentBackups();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void listRecentBackups_shouldIgnoreNonBackupFiles() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);
        Files.writeString(backupDirectory.resolve("honeyai-backup-2026-01-10-020000.db"), "backup");
        Files.writeString(backupDirectory.resolve("other-file.txt"), "not a backup");

        // When
        var result = backupService.listRecentBackups();

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void listRecentBackups_shouldReturnDtoWithCorrectFields() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);
        String filename = "honeyai-backup-2026-01-20-020000.db";
        Files.writeString(backupDirectory.resolve(filename), "backup data here");

        // When
        var result = backupService.listRecentBackups();

        // Then
        assertThat(result).hasSize(1);
        BackupFileDto dto = result.get(0);
        assertThat(dto.getFilename()).isEqualTo(filename);
        assertThat(dto.getLastModified()).isNotNull();
        assertThat(dto.getSizeBytes()).isGreaterThan(0);
        assertThat(dto.getFormattedSize()).contains("Ko");
    }

    // --- getBackupFile tests ---

    @Test
    void getBackupFile_shouldReturnPath_whenValidFilename() throws IOException {
        // Given
        Files.createDirectories(backupDirectory);
        String filename = "honeyai-backup-2026-01-10-020000.db";
        Files.writeString(backupDirectory.resolve(filename), "backup data");

        // When
        Path result = backupService.getBackupFile(filename);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFileName().toString()).isEqualTo(filename);
    }

    @Test
    void getBackupFile_shouldReturnNull_whenPathTraversal() {
        // When
        Path result = backupService.getBackupFile("../etc/passwd");

        // Then
        assertThat(result).isNull();
    }

    @Test
    void getBackupFile_shouldReturnNull_whenFilenameContainsSlash() {
        assertThat(backupService.getBackupFile("sub/honeyai-backup-2026-01-10-020000.db")).isNull();
    }

    @Test
    void getBackupFile_shouldReturnNull_whenFilenameContainsBackslash() {
        assertThat(backupService.getBackupFile("sub\\honeyai-backup-2026-01-10-020000.db")).isNull();
    }

    @Test
    void getBackupFile_shouldReturnNull_whenInvalidPattern() {
        assertThat(backupService.getBackupFile("malicious-file.db")).isNull();
    }

    @Test
    void getBackupFile_shouldReturnNull_whenFileDoesNotExist() {
        assertThat(backupService.getBackupFile("honeyai-backup-9999-01-01-020000.db")).isNull();
    }

    @Test
    void getBackupFile_shouldReturnNull_whenNull() {
        assertThat(backupService.getBackupFile(null)).isNull();
    }

    // --- getDatabasePath test ---

    @Test
    void getDatabasePath_shouldReturnConfiguredPath() {
        assertThat(backupService.getDatabasePath()).isEqualTo(databasePath);
    }

    // --- existing tests ---

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

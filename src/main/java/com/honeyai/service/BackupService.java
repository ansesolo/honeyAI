package com.honeyai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.honeyai.dto.BackupFileDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class BackupService {

    private static final DateTimeFormatter BACKUP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
    private static final int RETENTION_DAYS = 30;

    private final Path databasePath;
    private final Path backupDirectory;

    public BackupService(
            @Value("${honeyai.backup.database-path:./data/honeyai.db}") String databasePath,
            @Value("${honeyai.backup.directory:./backups}") String backupDirectory) {
        this.databasePath = Path.of(databasePath);
        this.backupDirectory = Path.of(backupDirectory);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledBackup() {
        performBackup();
    }

    public Path performBackup() {
        log.info("Demarrage du backup de la base de donnees...");

        try {
            if (!Files.exists(databasePath)) {
                log.warn("Fichier base de donnees introuvable: {}", databasePath);
                return null;
            }

            Files.createDirectories(backupDirectory);

            String timestamp = LocalDateTime.now().format(BACKUP_FORMAT);
            String backupFilename = "honeyai-backup-" + timestamp + ".db";
            Path backupPath = backupDirectory.resolve(backupFilename);

            Files.copy(databasePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Backup reussi: {}", backupPath);

            cleanupOldBackups();

            return backupPath;

        } catch (IOException e) {
            log.error("Erreur lors du backup: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<BackupFileDto> listRecentBackups() {
        if (!Files.exists(backupDirectory)) {
            return Collections.emptyList();
        }
        try (Stream<Path> files = Files.list(backupDirectory)) {
            return files
                    .filter(p -> p.getFileName().toString().startsWith("honeyai-backup-"))
                    .filter(p -> p.getFileName().toString().endsWith(".db"))
                    .sorted(Comparator.comparing(p -> {
                        try {
                            return Files.getLastModifiedTime((Path) p).toInstant();
                        } catch (IOException e) {
                            return java.time.Instant.EPOCH;
                        }
                    }).reversed())
                    .map(p -> {
                        try {
                            LocalDateTime modified = Files.getLastModifiedTime(p).toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
                            long size = Files.size(p);
                            return new BackupFileDto(p.getFileName().toString(), modified, size);
                        } catch (IOException e) {
                            return new BackupFileDto(p.getFileName().toString(), LocalDateTime.MIN, 0L);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            log.warn("Erreur lors de la lecture des backups: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Path getBackupFile(String filename) {
        if (filename == null || filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            log.warn("Tentative d'acces avec un nom de fichier invalide: {}", filename);
            return null;
        }
        if (!filename.startsWith("honeyai-backup-") || !filename.endsWith(".db")) {
            log.warn("Nom de fichier ne correspond pas au pattern de backup: {}", filename);
            return null;
        }
        Path filePath = backupDirectory.resolve(filename);
        if (!filePath.normalize().startsWith(backupDirectory.normalize())) {
            log.warn("Tentative de path traversal detectee: {}", filename);
            return null;
        }
        if (!Files.exists(filePath)) {
            log.warn("Fichier backup introuvable: {}", filename);
            return null;
        }
        return filePath;
    }

    public Path getDatabasePath() {
        return databasePath;
    }

    void cleanupOldBackups() {
        try (Stream<Path> files = Files.list(backupDirectory)) {
            LocalDateTime cutoff = LocalDateTime.now().minus(RETENTION_DAYS, ChronoUnit.DAYS);

            files.filter(p -> p.getFileName().toString().startsWith("honeyai-backup-"))
                    .filter(p -> p.getFileName().toString().endsWith(".db"))
                    .filter(p -> {
                        try {
                            return Files.getLastModifiedTime(p).toInstant()
                                    .isBefore(cutoff.atZone(java.time.ZoneId.systemDefault()).toInstant());
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                            log.info("Ancien backup supprime: {}", p.getFileName());
                        } catch (IOException e) {
                            log.warn("Impossible de supprimer l'ancien backup: {}", p, e);
                        }
                    });
        } catch (IOException e) {
            log.warn("Erreur lors du nettoyage des anciens backups: {}", e.getMessage());
        }
    }
}

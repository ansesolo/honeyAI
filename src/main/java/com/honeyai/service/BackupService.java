package com.honeyai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

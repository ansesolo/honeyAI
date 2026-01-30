package com.honeyai.controller;

import com.honeyai.dto.BackupFileDto;
import com.honeyai.service.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/backup")
@RequiredArgsConstructor
@Slf4j
public class BackupController {

    private final BackupService backupService;

    @GetMapping
    public String manage(Model model) {
        List<BackupFileDto> backups = backupService.listRecentBackups();
        model.addAttribute("backups", backups);
        model.addAttribute("activeMenu", "backup");
        return "backup/manage";
    }

    @PostMapping("/manual")
    public String manualBackup(RedirectAttributes redirectAttributes) {
        Path result = backupService.performBackup();
        if (result != null) {
            log.info("Backup manuel reussi: {}", result.getFileName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Sauvegarde creee avec succes : " + result.getFileName());
        } else {
            log.error("Echec du backup manuel");
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la creation de la sauvegarde. Verifiez les logs.");
        }
        return "redirect:/backup";
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String filename) throws IOException {
        Path filePath = backupService.getBackupFile(filename);
        if (filePath == null) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(filePath))
                .body(resource);
    }

    @GetMapping("/export-db")
    public ResponseEntity<Resource> exportDatabase() throws IOException {
        Path dbPath = backupService.getDatabasePath();
        if (!Files.exists(dbPath)) {
            log.warn("Base de donnees introuvable pour export: {}", dbPath);
            return ResponseEntity.notFound().build();
        }

        String exportFilename = "honeyai-export-" +
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".db";

        InputStreamResource resource = new InputStreamResource(Files.newInputStream(dbPath));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(Files.size(dbPath))
                .body(resource);
    }
}

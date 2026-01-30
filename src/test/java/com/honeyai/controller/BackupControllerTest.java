package com.honeyai.controller;

import com.honeyai.dto.BackupFileDto;
import com.honeyai.service.BackupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BackupController.class)
class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BackupService backupService;

    @TempDir
    Path tempDir;

    @Test
    void manage_shouldReturnManageView() throws Exception {
        // Given
        when(backupService.listRecentBackups()).thenReturn(Collections.emptyList());

        // When / Then
        mockMvc.perform(get("/backup"))
                .andExpect(status().isOk())
                .andExpect(view().name("backup/manage"))
                .andExpect(model().attributeExists("backups"))
                .andExpect(model().attribute("activeMenu", "backup"));
    }

    @Test
    void manage_shouldDisplayBackupsList() throws Exception {
        // Given
        BackupFileDto dto = new BackupFileDto("honeyai-backup-2026-01-30-020000.db",
                LocalDateTime.of(2026, 1, 30, 2, 0), 1024L);
        when(backupService.listRecentBackups()).thenReturn(List.of(dto));

        // When / Then
        mockMvc.perform(get("/backup"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("backups"));
    }

    @Test
    void manualBackup_shouldRedirectWithSuccess_whenBackupSucceeds() throws Exception {
        // Given
        Path backupPath = tempDir.resolve("honeyai-backup-2026-01-30-143000.db");
        Files.writeString(backupPath, "test");
        when(backupService.performBackup()).thenReturn(backupPath);

        // When / Then
        mockMvc.perform(post("/backup/manual"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/backup"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(backupService).performBackup();
    }

    @Test
    void manualBackup_shouldRedirectWithError_whenBackupFails() throws Exception {
        // Given
        when(backupService.performBackup()).thenReturn(null);

        // When / Then
        mockMvc.perform(post("/backup/manual"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/backup"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    void downloadBackup_shouldReturnFile_whenValid() throws Exception {
        // Given
        String filename = "honeyai-backup-2026-01-30-020000.db";
        Path backupFile = tempDir.resolve(filename);
        Files.writeString(backupFile, "backup content");
        when(backupService.getBackupFile(filename)).thenReturn(backupFile);

        // When / Then
        mockMvc.perform(get("/backup/download/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"" + filename + "\""));
    }

    @Test
    void downloadBackup_shouldReturn404_whenInvalidFilename() throws Exception {
        // Given
        when(backupService.getBackupFile("../etc/passwd")).thenReturn(null);

        // When / Then
        mockMvc.perform(get("/backup/download/{filename}", "../etc/passwd"))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadBackup_shouldReturn404_whenFileNotFound() throws Exception {
        // Given
        when(backupService.getBackupFile("honeyai-backup-9999-01-01-020000.db")).thenReturn(null);

        // When / Then
        mockMvc.perform(get("/backup/download/{filename}", "honeyai-backup-9999-01-01-020000.db"))
                .andExpect(status().isNotFound());
    }

    @Test
    void exportDb_shouldReturnDatabaseFile() throws Exception {
        // Given
        Path dbFile = tempDir.resolve("honeyai.db");
        Files.writeString(dbFile, "SQLite data");
        when(backupService.getDatabasePath()).thenReturn(dbFile);

        // When / Then
        mockMvc.perform(get("/backup/export-db"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.startsWith("attachment; filename=\"honeyai-export-")));
    }

    @Test
    void exportDb_shouldReturn404_whenDatabaseMissing() throws Exception {
        // Given
        Path missingDb = tempDir.resolve("nonexistent.db");
        when(backupService.getDatabasePath()).thenReturn(missingDb);

        // When / Then
        mockMvc.perform(get("/backup/export-db"))
                .andExpect(status().isNotFound());
    }
}

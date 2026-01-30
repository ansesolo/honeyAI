package com.honeyai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BackupFileDto {
    private String filename;
    private LocalDateTime lastModified;
    private long sizeBytes;

    public String getFormattedSize() {
        if (sizeBytes >= 1_048_576) {
            return String.format("%.1f Mo", sizeBytes / 1_048_576.0);
        }
        return String.format("%.1f Ko", sizeBytes / 1_024.0);
    }
}

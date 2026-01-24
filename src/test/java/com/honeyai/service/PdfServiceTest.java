package com.honeyai.service;

import com.honeyai.exception.PdfGenerationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfServiceTest {

    private PdfService pdfService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService();
    }

    @Test
    void createDocument_shouldReturnNewPDDocument() throws IOException {
        // When
        try (PDDocument document = pdfService.createDocument()) {
            // Then
            assertThat(document).isNotNull();
            assertThat(document.getNumberOfPages()).isZero();
        }
    }

    @Test
    void createPage_shouldReturnPageWithSpecifiedSize() {
        // When
        PDPage page = pdfService.createPage(PDRectangle.A4);

        // Then
        assertThat(page).isNotNull();
        assertThat(page.getMediaBox().getWidth()).isEqualTo(PDRectangle.A4.getWidth());
        assertThat(page.getMediaBox().getHeight()).isEqualTo(PDRectangle.A4.getHeight());
    }

    @Test
    void createA4Page_shouldReturnA4SizedPage() {
        // When
        PDPage page = pdfService.createA4Page();

        // Then
        assertThat(page).isNotNull();
        assertThat(page.getMediaBox().getWidth()).isEqualTo(PDRectangle.A4.getWidth());
        assertThat(page.getMediaBox().getHeight()).isEqualTo(PDRectangle.A4.getHeight());
    }

    @Test
    void addTextToPage_shouldAddTextWithoutException() throws IOException {
        // Given
        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When/Then - no exception
            pdfService.addTextToPage(document, page, "Test text", 50, 700, 12);
        }
    }

    @Test
    void addBoldTextToPage_shouldAddBoldTextWithoutException() throws IOException {
        // Given
        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When/Then - no exception
            pdfService.addBoldTextToPage(document, page, "Bold test", 50, 700, 14);
        }
    }

    @Test
    void generateTestPdf_shouldCreatePdfFile() throws IOException {
        // Given
        Path outputPath = tempDir.resolve("test-output.pdf");

        // When
        pdfService.generateTestPdf(outputPath);

        // Then
        assertThat(Files.exists(outputPath)).isTrue();
        assertThat(Files.size(outputPath)).isGreaterThan(0);
    }

    @Test
    void generateTestPdf_shouldCreateValidPdf() throws IOException {
        // Given
        Path outputPath = tempDir.resolve("test-output.pdf");

        // When
        pdfService.generateTestPdf(outputPath);

        // Then - verify PDF is valid and can be opened
        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            assertThat(document.getNumberOfPages()).isEqualTo(1);
        }
    }

    @Test
    void generateTestPdf_withMultipleTextBlocks_shouldCreateValidPdf() throws IOException {
        // Given
        Path outputPath = tempDir.resolve("multi-text.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - add multiple text blocks at different positions
            pdfService.addTextToPage(document, page, "Title", 50, 750, 24);
            pdfService.addTextToPage(document, page, "Line 1", 50, 700, 12);
            pdfService.addTextToPage(document, page, "Line 2", 50, 680, 12);
            pdfService.addTextToPage(document, page, "Line 3", 50, 660, 12);
            pdfService.addBoldTextToPage(document, page, "Bold Line", 50, 620, 14);

            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();
        assertThat(Files.size(outputPath)).isGreaterThan(0);

        // Verify PDF can be opened and read
        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            assertThat(document.getNumberOfPages()).isEqualTo(1);
        }
    }

    @Test
    void mmToPoints_shouldConvertCorrectly() {
        // 1 mm = 2.83465 points
        assertThat(pdfService.mmToPoints(1)).isCloseTo(2.83465f, org.assertj.core.api.Assertions.within(0.001f));
        assertThat(pdfService.mmToPoints(10)).isCloseTo(28.3465f, org.assertj.core.api.Assertions.within(0.01f));
        assertThat(pdfService.mmToPoints(100)).isCloseTo(283.465f, org.assertj.core.api.Assertions.within(0.1f));
    }

    @Test
    void pointsToMm_shouldConvertCorrectly() {
        assertThat(pdfService.pointsToMm(2.83465f)).isCloseTo(1f, org.assertj.core.api.Assertions.within(0.001f));
        assertThat(pdfService.pointsToMm(72f)).isCloseTo(25.4f, org.assertj.core.api.Assertions.within(0.1f));
    }

    @Test
    void pointsToMm_andMmToPoints_shouldBeInverse() {
        float originalMm = 50f;
        float points = pdfService.mmToPoints(originalMm);
        float backToMm = pdfService.pointsToMm(points);

        assertThat(backToMm).isCloseTo(originalMm, org.assertj.core.api.Assertions.within(0.001f));
    }
}

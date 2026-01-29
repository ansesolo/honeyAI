package com.honeyai.service;

import com.honeyai.config.EtiquetteConfig;
import com.honeyai.config.LabelPreset;
import com.honeyai.dto.EtiquetteData;
import com.honeyai.exception.PdfGenerationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfServiceTest {

    private PdfService pdfService;
    private EtiquetteConfig etiquetteConfig;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        etiquetteConfig = new EtiquetteConfig();
        etiquetteConfig.setSiret("12345678901234");
        etiquetteConfig.setNomApiculteur("Test Apiculteur");
        etiquetteConfig.setAdresse("123 Test Street");
        etiquetteConfig.setTelephone("0612345678");
        etiquetteConfig.setDluoDureeJours(730);
        etiquetteConfig.setLabelWidthMm(60.0f);
        etiquetteConfig.setLabelHeightMm(40.0f);
        etiquetteConfig.setLabelsPerRow(3);
        etiquetteConfig.setLabelsPerColumn(7);
        etiquetteConfig.setMarginTopMm(10.0f);
        etiquetteConfig.setMarginLeftMm(10.0f);

        pdfService = new PdfService(etiquetteConfig);
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

    // ==================== renderLabel Tests ====================

    private EtiquetteData createTestLabelData() {
        return EtiquetteData.builder()
                .typeMiel("Toutes Fleurs")
                .formatPot("500g")
                .poids("Poids net: 500g")
                .nomApiculteur("Exploitation Apicole Familiale")
                .adresse("123 Rue de la Ruche, 12345 Village, France")
                .siret("12345678901234")
                .telephone("06 12 34 56 78")
                .dluo(LocalDate.of(2026, 8, 15))

                .prixUnitaire(new BigDecimal("8.50"))
                .build();
    }

    @Test
    void renderLabel_shouldCreatePdfWithLabelContent() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();
        Path outputPath = tempDir.resolve("label-test.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When
            pdfService.renderLabel(document, page, data, 50, 700, 60, 40);
            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();
        assertThat(Files.size(outputPath)).isGreaterThan(0);

        // Verify PDF content contains expected text
        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("FRANCE");
            assertThat(text).contains("Poids net: 500g");
            assertThat(text).contains("SIRET: 12345678901234");
            assertThat(text).contains("8.50 EUR");
        }
    }

    @Test
    void renderLabel_shouldRenderWithDefaultDimensions() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();
        Path outputPath = tempDir.resolve("label-default-size.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - use default dimensions from config
            pdfService.renderLabel(document, page, data, 100, 600);
            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();
    }

    @Test
    void renderLabel_shouldHandleFrenchAccents() throws IOException {
        // Given
        EtiquetteData data = EtiquetteData.builder()
                .typeMiel("Châtaignier")
                .formatPot("500g")
                .poids("Poids net: 500g")
                .nomApiculteur("René Éléonore")
                .adresse("5 Rue des Étoiles, Montréal")
                .siret("12345678901234")
                .telephone("06 12 34 56 78")
                .dluo(LocalDate.of(2026, 8, 15))

                .prixUnitaire(new BigDecimal("12.00"))
                .build();

        Path outputPath = tempDir.resolve("label-accents.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - no exception should occur with French accents
            pdfService.renderLabel(document, page, data, 50, 700, 60, 40);
            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();

        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("FRANCE");
        }
    }

    @Test
    void renderLabel_shouldHandleNullOptionalFields() throws IOException {
        // Given - minimal data with some null fields
        EtiquetteData data = EtiquetteData.builder()
                .typeMiel("Forêt")
                .formatPot("1kg")
                .nomApiculteur("Test")
                .siret("12345678901234")
                .dluo(LocalDate.of(2026, 12, 31))

                // telephone is null
                // prixUnitaire is null
                // adresse is null
                .build();

        Path outputPath = tempDir.resolve("label-minimal.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - should not throw exception
            pdfService.renderLabel(document, page, data, 50, 700, 60, 40);
            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();
    }

    @Test
    void renderLabel_shouldSplitLongAddress() throws IOException {
        // Given - address with comma (will be split into 2 lines)
        EtiquetteData data = EtiquetteData.builder()
                .typeMiel("Toutes Fleurs")
                .formatPot("500g")
                .nomApiculteur("Test")
                .adresse("123 Rue des Abeilles, 12345 Ville")
                .siret("12345678901234")
                .dluo(LocalDate.of(2026, 8, 15))

                .build();

        Path outputPath = tempDir.resolve("label-split-address.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - should split address on comma
            pdfService.renderLabel(document, page, data, 50, 700, 60, 40);
            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();

        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            // Both parts of address should be present
            assertThat(text).contains("123 Rue des Abeilles");
            assertThat(text).contains("12345 Ville");
        }
    }

    @Test
    void generateSingleLabelPdf_shouldCreateCenteredLabel() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();
        Path outputPath = tempDir.resolve("single-label.pdf");

        // When
        pdfService.generateSingleLabelPdf(data, outputPath);

        // Then
        assertThat(Files.exists(outputPath)).isTrue();

        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            assertThat(document.getNumberOfPages()).isEqualTo(1);

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("FRANCE");
        }
    }

    @Test
    void renderLabel_shouldRenderMultipleLabelsOnSamePage() throws IOException {
        // Given
        EtiquetteData data1 = createTestLabelData();
        EtiquetteData data2 = EtiquetteData.builder()
                .typeMiel("Forêt")
                .formatPot("1kg")
                .nomApiculteur("Autre Apiculteur")
                .siret("98765432109876")
                .dluo(LocalDate.of(2027, 1, 15))

                .prixUnitaire(new BigDecimal("15.00"))
                .build();

        Path outputPath = tempDir.resolve("multi-labels.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            // When - render two labels at different positions
            pdfService.renderLabel(document, page, data1, 50, 700, 60, 40);
            pdfService.renderLabel(document, page, data2, 250, 700, 60, 40);

            document.save(outputPath.toFile());
        }

        // Then
        assertThat(Files.exists(outputPath)).isTrue();

        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Both labels should be present
            assertThat(text).contains("FRANCE");
            assertThat(text).contains("15.00 EUR");
        }
    }

    @Test
    void renderLabel_shouldIncludeDluoFormatted() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();
        data.setDluo(LocalDate.of(2026, 8, 15));

        Path outputPath = tempDir.resolve("label-dluo.pdf");

        try (PDDocument document = pdfService.createDocument()) {
            PDPage page = pdfService.createA4Page();
            document.addPage(page);

            pdfService.renderLabel(document, page, data, 50, 700, 60, 40);
            document.save(outputPath.toFile());
        }

        // Then
        try (PDDocument document = Loader.loadPDF(outputPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("A consommer de preference avant fin:");
            assertThat(text).contains("08/2026");
        }
    }

    // ==================== Multi-Label Sheet Tests ====================

    @Test
    void generateEtiquetteSheet_shouldGenerateOneFullPage() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();

        // When
        byte[] pdfBytes = pdfService.generateEtiquetteSheet(data);

        // Then
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            assertThat(document.getNumberOfPages()).isEqualTo(1);

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("FRANCE");
            assertThat(text).contains("8.50 EUR");
        }
    }

    @Test
    void generateEtiquetteSheet_shouldCompleteWithin5Seconds() {
        // Given
        EtiquetteData data = createTestLabelData();

        // When
        long startTime = System.currentTimeMillis();
        byte[] pdfBytes = pdfService.generateEtiquetteSheet(data);
        long duration = System.currentTimeMillis() - startTime;

        // Then
        assertThat(duration).isLessThan(5000);
        assertThat(pdfBytes).isNotNull();
    }

    @Test
    void generateEtiquetteSheet_shouldThrowException_forNullData() {
        // When/Then
        assertThatThrownBy(() -> pdfService.generateEtiquetteSheet(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("données d'étiquette");
    }

    @Test
    void getLabelsPerPage_shouldReturnConfigValue() {
        // The config is set to 3x7 = 21
        assertThat(pdfService.getLabelsPerPage()).isEqualTo(21);
    }

    // ==================== Preset-based Sheet Tests ====================

    @Test
    void generateEtiquetteSheet_withPreset_shouldGenerateOneFullPage() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();
        LabelPreset preset = LabelPreset.builder()
                .name("Grand (70x50mm - 2x5)")
                .labelWidthMm(70.0f)
                .labelHeightMm(50.0f)
                .labelsPerRow(2)
                .labelsPerColumn(5)
                .marginTopMm(15.0f)
                .marginLeftMm(25.0f)
                .build();

        // When
        byte[] pdfBytes = pdfService.generateEtiquetteSheet(data, preset);

        // Then
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            assertThat(document.getNumberOfPages()).isEqualTo(1);

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            assertThat(text).contains("FRANCE");
            assertThat(text).contains("8.50 EUR");
        }
    }

    @Test
    void generateEtiquetteSheet_withNullPreset_shouldFallbackToConfig() throws IOException {
        // Given
        EtiquetteData data = createTestLabelData();

        // When
        byte[] pdfBytes = pdfService.generateEtiquetteSheet(data, null);

        // Then
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);
    }

    @Test
    void generateEtiquetteSheet_withPreset_shouldThrowException_forNullData() {
        // Given
        LabelPreset preset = LabelPreset.builder()
                .name("Test")
                .labelWidthMm(60.0f)
                .labelHeightMm(40.0f)
                .labelsPerRow(3)
                .labelsPerColumn(7)
                .marginTopMm(10.0f)
                .marginLeftMm(10.0f)
                .build();

        // When/Then
        assertThatThrownBy(() -> pdfService.generateEtiquetteSheet(null, preset))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("données d'étiquette");
    }
}

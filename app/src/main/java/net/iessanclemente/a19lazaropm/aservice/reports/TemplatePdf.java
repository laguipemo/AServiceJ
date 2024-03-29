package net.iessanclemente.a19lazaropm.aservice.reports;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import net.iessanclemente.a19lazaropm.aservice.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TemplatePdf {
    private final Font FONT_SUBTITLE = new Font(Font.FontFamily.HELVETICA, 18.0f, 1);
    private final Font FONT_TEXT = new Font(Font.FontFamily.HELVETICA, 12.0f);
    private final Font FONT_TEXT_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);
    private final Font FONT_TEXT_BOLDITALIC = new Font(Font.FontFamily.HELVETICA, 12.0f, 3);
    private final Font FONT_TEXT_BOLD_15 = new Font(Font.FontFamily.HELVETICA, 15.0f, 1);
    private final Font FONT_TEXT_BOLD_BLUE = new Font(Font.FontFamily.HELVETICA, 12.0f, 1, BaseColor.BLUE);
    private final Font FONT_TEXT_BOLD_RED = new Font(Font.FontFamily.HELVETICA, 12.0f, 1, BaseColor.RED);
    private final Font FONT_TEXT_ITALIC = new Font(Font.FontFamily.HELVETICA, 12.0f, 2);
    private final Font FONT_TEXT_LITTLE = new Font(Font.FontFamily.HELVETICA, 10.0f);
    private final Font FONT_TEXT_LITTLE_BOLD = new Font(Font.FontFamily.HELVETICA, 10.0f, 1);
    private final Font FONT_TEXT_LITTLE_BOLDITALIC = new Font(Font.FontFamily.HELVETICA, 10.0f, 3);
    private final Font FONT_TEXT_LITTLE_ITALIC = new Font(Font.FontFamily.HELVETICA, 10.0f, 2);
    private final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 20.0f, 1);
    private final Context context;
    private Document document;
    private Paragraph paragraph;
    private File pdfFile;
    private PdfWriter pdfWriter;
    private final File pdfsFolder;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private class PageBackgroundAtlas extends PdfPageEventHelper {
        private PageBackgroundAtlas() {
        }

        public void onStartPage(PdfWriter pdfWriter, Document document) {
            addImageDrawableAsBackgroud(R.drawable.fondo_informe_atlas);
        }
    }

    public TemplatePdf(Context context, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.context = context;
        this.activityResultLauncher = activityResultLauncher;
        pdfsFolder = new File(context.getFilesDir(), "reports");
    }

    private void createFile(String fileName) {
        if (pdfsFolder.exists() || pdfsFolder.mkdirs()) {
            pdfFile = new File(pdfsFolder, fileName);
        } else {
            Toast.makeText(this.context, "No se pudo crear directorio", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDocument(String fileName) {
        createFile(fileName);
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            pdfWriter.setPageEvent(new PageBackgroundAtlas());
            document.open();
        } catch (Exception e) {
            Log.e("Error in openDocument: ", e.toString());
        }
    }

    public void openDocument() {
        openDocument("InformeVitrina.pdf");
    }

    public void openDocumetStamperPageNumering(String fileName) {
        createFile(fileName);
        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(pdfFile));
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(pdfFile));
            int numberOfPages = pdfReader.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                PdfContentByte underContent = pdfStamper.getUnderContent(i);
                String format = String.format(
                        Locale.getDefault(), "Pagina %d de %d", i, numberOfPages);
                BaseFont createFont = BaseFont.createFont(
                        "Helvetica-Oblique", "Cp1252", false);
                float width = pdfReader.getPageSize(i).getWidth();
                pdfReader.getPageSize(i).getHeight();
                underContent.saveState();
                underContent.beginText();
                underContent.setFontAndSize(createFont, 12.0f);
                underContent.setTextMatrix((width / 2.0f) + 200.0f, 45.0f);
                underContent.showText(format);
                underContent.endText();
                underContent.restoreState();
            }
            pdfStamper.close();
        } catch (DocumentException | IOException e) {
            Log.e("Error in openDocumentStamper", e.toString());
        }
    }

    public void openDocumetStamperPageNumering() {
        openDocumetStamperPageNumering("InformeVitrina.pdf");
    }

    public void closeDocument() {
        document.close();
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void addMetaData(String title, String subject, String author) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String titleInforme, String titleVitrina) {
        paragraph = new Paragraph();
        addChildParagraphCenter(
                new Paragraph(titleInforme, FONT_TITLE), 0.0f, 10.0f);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(20.0f);
        PdfPTable pdfPTable = new PdfPTable(1);
        pdfPTable.setWidthPercentage(50.0f);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(titleVitrina, FONT_TITLE));
        pdfPCell.setFixedHeight(40.0f);
        pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPCell.setHorizontalAlignment(1);
        pdfPCell.setVerticalAlignment(5);
        pdfPTable.addCell(pdfPCell);
        paragraph2.add(pdfPTable);
        addChildParagraphCenter(paragraph2, 10.0f, 10.0f);
        paragraph.setSpacingAfter(30.0f);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addTitles: ", e.toString());
        }
    }

    public void addDatosEmpresa(
            String fecha, String clienteName, String direccion, String contactoName,
            String contactoTelefono, String contactoEmail) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(10.0f);
        paragraph2.add(new Chunk("Fecha: ", FONT_TEXT_BOLD));
        paragraph2.add(new Chunk(fecha, FONT_TEXT));
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.setSpacingBefore(10.0f);
        paragraph3.add(new Chunk("Cliente: ", FONT_TEXT_BOLD));
        paragraph3.add(new Chunk(clienteName, FONT_TEXT));
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.setSpacingBefore(10.0f);
        paragraph4.add(new Chunk("Dirección: ", FONT_TEXT_BOLD));
        paragraph4.add(new Chunk(direccion, FONT_TEXT));
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.setSpacingBefore(10.0f);
        paragraph5.add(new Chunk("Persona de contacto: ", this.FONT_TEXT_BOLD));
        paragraph5.add(new Chunk(contactoName, FONT_TEXT));
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.setSpacingBefore(10.0f);
        paragraph6.add(new Chunk("Telefono: ", FONT_TEXT_BOLD));
        paragraph6.add(new Chunk(contactoTelefono, FONT_TEXT));
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.setSpacingBefore(10.0f);
        paragraph7.add(new Chunk("E-mail: ", FONT_TEXT_BOLD));
        paragraph7.add(new Chunk(contactoEmail, FONT_TEXT));
        addChildParagraphLeft(paragraph7);
        paragraph.setSpacingBefore(50.0f);
        paragraph.setSpacingAfter(80.0f);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addDatosEmpresa: ", e.toString());
        }
    }

    public void addDatosVitrina(
            String fabricanteName, String vitrinaTipo, String vitrinaRef, String vitrinaAnno,
            String vitrinaNumSerie, String vitrinaNumInvent, boolean isPuestaEnMarcha,
            String mantenimiento, String tecnicoName) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk("Fabricante: ", FONT_TEXT_BOLD));
        paragraph2.add(new Chunk(fabricanteName, FONT_TEXT));
        paragraph2.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(new Chunk("Tipo: ", FONT_TEXT_BOLD));
        paragraph3.add(new Chunk(vitrinaTipo, FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add(new Chunk("Referencia: ", FONT_TEXT_BOLD));
        paragraph4.add(new Chunk(vitrinaRef, FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add(new Chunk("Año construcción: ", FONT_TEXT_BOLD));
        paragraph5.add(new Chunk(vitrinaAnno, FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add(new Chunk("Nº de serie: ", FONT_TEXT_BOLD));
        paragraph6.add(new Chunk(vitrinaNumSerie, FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add(new Chunk("Nº de inventario: ", FONT_TEXT_BOLD));
        paragraph7.add(new Chunk(vitrinaNumInvent, this.FONT_TEXT));
        paragraph7.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.add(new Chunk("Puesta en marcha: ", FONT_TEXT_BOLD));
        paragraph8.add(new Chunk(isPuestaEnMarcha ? "Si" : "No", FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add(new Chunk("Servicio de mantenimiento: ", FONT_TEXT_BOLD));
        paragraph9.add(new Chunk(mantenimiento, FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add(new Chunk("Técnico responsable: ", FONT_TEXT_BOLD));
        paragraph10.add(new Chunk(tecnicoName, FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addDatosVitrina: ", e.toString());
        }
    }

    public void addComprobAjusteSistemaExtracion(
            String funcControlDigital, String inspecVisSistemaExtraccion, boolean isSegunDin,
            boolean isSegunEn, int vitrinaLong, float guilloLong) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk("Comprobación y ajuste del sistema de extracción", FONT_SUBTITLE));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(20.0f);
        addChildParagraphCenter(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(new Chunk("Funcionamiento del controlador digital: ", FONT_TEXT_BOLD));
        paragraph3.add(new Chunk(funcControlDigital, FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add(new Chunk("Inspección visual del sistema de extracción: ", FONT_TEXT_BOLD));
        paragraph4.add(new Chunk(inspecVisSistemaExtraccion, FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add(new Chunk("Requerimiento según norma DIN 12924: ", FONT_TEXT_BOLD));
        paragraph5.add(new Chunk(isSegunDin ? "Si" : "No", FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add(new Chunk("Requerimiento según norma EN 14175: ", FONT_TEXT_BOLD));
        paragraph6.add(new Chunk(isSegunEn ? "Si" : "No", FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add(new Chunk("Medición de volumen de extracción", FONT_TEXT_BOLD_15));
        paragraph7.setSpacingBefore(30.0f);
        paragraph7.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.add(new Chunk("Dimensiones de la Vitrina de Gases: ", FONT_TEXT_BOLD));
        paragraph8.add(new Chunk(String.valueOf(vitrinaLong), FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add(new Chunk("Longitud de la Guillotina: ", FONT_TEXT_BOLD));
        paragraph9.add(new Chunk(String.valueOf(guilloLong), FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add(new Chunk("Altura de trabajo: ", FONT_TEXT_BOLD));
        paragraph10.add(new Chunk("500", FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComprobAjusteSistemaExtracion: ", e.toString());
        }
    }

    public void createTableMediciones(float[] arrayValoresMedicion) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk("Velocidades en frontal (m/s)", FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph2);

        paragraph.setFont(this.FONT_TEXT);

        PdfPTable pdfPTable = new PdfPTable(arrayValoresMedicion.length -1);
        pdfPTable.setWidthPercentage(100.0f);
        for (int indiceValor = 1; indiceValor < arrayValoresMedicion.length; indiceValor++) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(String.valueOf(indiceValor), FONT_TEXT_BOLD));
            pdfPCell.setFixedHeight(25.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPTable.addCell(pdfPCell);
        }
        for (int indiceValor = 1; indiceValor < arrayValoresMedicion.length; indiceValor++) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(
                    String.format(Locale.getDefault(), "%.2f", arrayValoresMedicion[indiceValor]),
                    FONT_TEXT));
            pdfPCell.setFixedHeight(40.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPTable.addCell(pdfPCell);
        }
        paragraph.add(pdfPTable);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(new Chunk("Velocidad media (m/s): ", FONT_TEXT_BOLD));
        paragraph3.add(new Chunk(
                String.format(Locale.getDefault(), "%.2f", arrayValoresMedicion[0]),
                FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph3);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTableMediciones: ", e.toString());
        }
    }

    public void creatTableCalcVolExtraccion(
            float guilloLong, float velocidadAire, int minVal, int maxVal, int recomendedVal) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk(
                "Cálculo del volumen de extracción", FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(new Chunk(
                "Volumen de extracción (m³/h) = Longitud Gillotina (m) x Altura Trabajo (m) x Velocidad Aire (m/seg) x 3600 seg",
                FONT_TEXT_LITTLE_ITALIC));
        paragraph3.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph3);
        paragraph.setFont(FONT_TEXT);
        String[] arrayColumnTitles = {
                "Longitud\nGillotina\n(m)", "Altura de\nTrabajo\n(m)",
                "Velocidad\ndel Aire\n(m/seg)", "Factor de\nconversión\n(seg)",
                "Volumen de\nextracción\n(m³/h)"};
        PdfPTable pdfPTable = new PdfPTable(arrayColumnTitles.length);
        pdfPTable.setWidthPercentage(100.0f);
        for (String columnTitle : arrayColumnTitles) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(columnTitle, FONT_TEXT_BOLD));
            pdfPCell.setFixedHeight(60.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPTable.addCell(pdfPCell);
        }
        Phrase[] phraseArr = {
                new Phrase(
                        String.format(Locale.getDefault(), "%.2f", guilloLong),
                        FONT_TEXT),
                new Phrase(
                        String.format(Locale.getDefault(), "%.2f", 0.5f),
                        FONT_TEXT),
                new Phrase(
                        String.format(Locale.getDefault(), "%.2f", velocidadAire),
                        this.FONT_TEXT),
                new Phrase(
                        String.format(Locale.getDefault(), "%d", 3600), FONT_TEXT),
                new Phrase(
                        String.format(Locale.getDefault(), "%.2f",
                                guilloLong * 0.5f * velocidadAire * 3600.0f),
                        FONT_TEXT_BOLD_15)};

        for (Phrase elements : phraseArr) {
            PdfPCell pdfPCell = new PdfPCell(elements);
            pdfPCell.setFixedHeight(40.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPTable.addCell(pdfPCell);
        }
        paragraph.add(pdfPTable);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.setTabSettings(new TabSettings(40.0f));
        paragraph4.add(new Chunk("Valores teóricos (m/s): ", FONT_TEXT_BOLD));
        paragraph4.add(Chunk.TABBING);
        paragraph4.add(new Chunk(
                String.format(Locale.getDefault(), "Min = %d ", minVal),
                FONT_TEXT));
        paragraph4.add(Chunk.TABBING);
        paragraph4.add(new Chunk(
                String.format(Locale.getDefault(), "Máx = %d ", maxVal),
                FONT_TEXT));
        paragraph4.add(Chunk.TABBING);
        paragraph4.add(new Chunk(
                String.format(Locale.getDefault(), "Recomendado = %d ", recomendedVal),
                FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph4);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTableCalcVolExtraccion: ", e.toString());
        }
    }

    public void addComprobAjusteComponentesFijos(
            String protecSuperficie, String aislaJuntas, String piezasFijas, String funcGuillo,
            String estadoGnrlGuillo, float fuerzaGuillo, String evaluFuerzaGuillo,
            String ctrlPresencia, String autoProtect, String grifosMonored, float valorLight,
            String light, float valorSound, String sound, String intrumentosMedida) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk(
                "Comprobación y ajuste de componentes mecánicos y medios (grifos/manoreductores)",
                FONT_SUBTITLE));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(20.0f);
        addChildParagraphCenter(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add(new Chunk(
                "Protección de la superficie: ", FONT_TEXT_BOLD));
        paragraph3.add(new Chunk(protecSuperficie, FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add(new Chunk(
                "Aislamiento de las juntas en el interior: ", FONT_TEXT_BOLD));
        paragraph4.add(new Chunk(aislaJuntas, FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add(new Chunk(
                "Sujeción correcta de las piezas fijas: ", FONT_TEXT_BOLD));
        paragraph5.add(new Chunk(piezasFijas, FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add(new Chunk(
                "Funcionamiento de la guillotina: ", FONT_TEXT_BOLD));
        paragraph6.add(new Chunk(funcGuillo, FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add(new Chunk(
                "Estado general de la guillotina: ", FONT_TEXT_BOLD));
        paragraph7.add(new Chunk(estadoGnrlGuillo, FONT_TEXT));
        paragraph7.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.setTabSettings(new TabSettings(20.0f));
        paragraph8.add(new Chunk(
                "Fuerza para el movimiento vertical de la guillotina: ", FONT_TEXT_BOLD));
        paragraph8.add(new Chunk(
                String.format(Locale.getDefault(), "%.2f N ", fuerzaGuillo),
                FONT_TEXT));
        paragraph8.add(Chunk.TABBING);
        paragraph8.add(new Chunk(evaluFuerzaGuillo, FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add(new Chunk("Control de presencia: ", FONT_TEXT_BOLD));
        paragraph9.add(new Chunk(ctrlPresencia, FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add(new Chunk("Autoproteccion: ", FONT_TEXT_BOLD));
        paragraph10.add(new Chunk(autoProtect, FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        Paragraph paragraph11 = new Paragraph();
        paragraph11.add(new Chunk(
                "Compobación del funcionamiento de grifos/monoreductores: ",
                FONT_TEXT_BOLD));
        paragraph11.add(new Chunk(grifosMonored, FONT_TEXT));
        paragraph11.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph11);

        Paragraph paragraph12 = new Paragraph();
        paragraph12.add(new Chunk(
                "Comprobación de iluminación y nivel de ruido: ",
                FONT_TEXT_BOLD));
        paragraph12.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph12);
        Paragraph paragraph13 = new Paragraph();
        paragraph13.setIndentationLeft(30.0f);
        paragraph13.add(new Chunk("Iluminación (> 400 lux): ", FONT_TEXT_BOLD));
        paragraph13.add(new Chunk(
                String.format(Locale.getDefault(), "%.2f lux ", valorLight), FONT_TEXT));
        paragraph13.add(Chunk.TABBING);
        paragraph13.add(new Chunk(light, FONT_TEXT));
        paragraph13.setSpacingBefore(5.0f);
        addChildParagraphLeft(paragraph13);
        Paragraph paragraph14 = new Paragraph();
        paragraph14.setIndentationLeft(30.0f);
        paragraph14.add(new Chunk("Sonido (< 70 dBA): ", FONT_TEXT_BOLD));
        paragraph14.add(new Chunk(
                String.format(Locale.getDefault(), "%.2f dBA ", valorSound), FONT_TEXT));
        paragraph14.add(Chunk.TABBING);
        paragraph14.add(new Chunk(sound, FONT_TEXT));
        paragraph14.setSpacingBefore(5.0f);
        addChildParagraphLeft(paragraph14);

        Paragraph paragraph15 = new Paragraph();
        paragraph15.add(new Chunk(
                "Instrumentos de medida utilizados: ",
                FONT_TEXT_BOLD));
        paragraph15.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph15);
        Paragraph paragraph16 = new Paragraph();
        paragraph16.setIndentationLeft(30.0f);
        paragraph16.add(new Chunk(intrumentosMedida, FONT_TEXT));
        paragraph16.setSpacingBefore(5.0f);
        addChildParagraphLeft(paragraph16);

        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComprobAjusteComponentesFijos: ", e.toString());
        }
    }

    public void addComentAndFinalEvalu(String comentario, boolean isNormasOk, boolean isNeedReparation) {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add(new Chunk("Comentarios: ", FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.setIndentationLeft(30.0f);
        paragraph3.add(new Paragraph(comentario, FONT_TEXT));
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add(new Chunk("Resultado: ", FONT_TEXT_BOLD));
        paragraph4.setSpacingBefore(20.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.setIndentationLeft(30.0f);
        paragraph5.add(new Chunk(
                "El equipo está de acuerdo con las normas / regulaciones pertinentes: ",
                FONT_TEXT));
        paragraph5.add(new Chunk(isNormasOk ? "Si" : "No", FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.setIndentationLeft(30.0f);
        paragraph6.add(new Chunk(
                "Por cuestiones de seguridad es necesaria la intervención del equipo: ",
                FONT_TEXT));
        paragraph6.add(new Chunk(isNeedReparation ? "Si" : "No", FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComentAndFinalEvalu: ", e.toString());
        }
    }

    public void addSignatures() {
        paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(60.0f);
        paragraph2.setIndentationLeft(30.0f);
        paragraph2.setTabSettings(new TabSettings(300.0f));
        paragraph2.add(new Chunk("Firma Técnico", FONT_TEXT_BOLD));
        paragraph2.add(Chunk.TABBING);
        paragraph2.add(new Chunk("Firma de la propiedad", FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph2);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addSignatures: ", e.toString());
        }
    }

    public void addChildParagraphCenter(Paragraph paragraphToAdd) {
        addChildParagraphCenter(paragraphToAdd, 0.0f, 0.0f);
    }

    public void addChildParagraphCenter(Paragraph paragraphToAdd, float spacingBefore, float spacingAfter) {
        paragraphToAdd.setAlignment(1);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.add(paragraphToAdd);
        paragraph.setSpacingAfter(spacingAfter);
    }

    public void addChildParagraphLeft(Paragraph paragraphToAdd) {
        addChildParagraphLeft(paragraphToAdd, 0.0f, 0.0f);
    }

    public void addChildParagraphLeft(Paragraph paragraphToAdd, float spacingBefore, float spacingAfter) {
        paragraphToAdd.setAlignment(0);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.add(paragraphToAdd);
        paragraph.setSpacingAfter(spacingAfter);
    }

    public void addChildParagraphRight(Paragraph paragraphToAdd) {
        addChildParagraphRight(paragraphToAdd, 0.0f, 0.0f);
    }

    public void addChildParagraphRight(Paragraph paragraphToAdd, float spacingBefore, float spacingAfter) {
        paragraphToAdd.setAlignment(2);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.add(paragraphToAdd);
        paragraph.setSpacingAfter(spacingAfter);
    }

    public void addChildParagraphJustified(Paragraph paragraphToAdd) {
        addChildParagraphJustified(paragraphToAdd, 0.0f, 0.0f);
    }

    public void addChildParagraphJustified(Paragraph paragraphToAdd, float spacingBefore, float spacingAfter) {
        paragraphToAdd.setAlignment(3);
        paragraph.setSpacingBefore(spacingBefore);
        paragraph.add(paragraphToAdd);
        paragraph.setSpacingAfter(spacingAfter);
    }

    public void addChildParagraphAligned(Paragraph paragraphToAdd, int align) {
        addChildParagraphAligned(paragraphToAdd, align, 0.0f, 0.0f);
    }

    public void addChildParagraphAligned(Paragraph paragraphToAdd, int align, float spacingBefore, float spacingAfter) {
        paragraph.setSpacingBefore(spacingBefore);
        if (align == 0) {
            addChildParagraphLeft(paragraphToAdd);
        } else if (align == 1) {
            addChildParagraphCenter(paragraphToAdd);
        } else if (align == 2) {
            addChildParagraphRight(paragraphToAdd);
        } else if (align != 3) {
            paragraphToAdd.setAlignment(align);
            paragraph.add(paragraphToAdd);
        } else {
            addChildParagraphJustified(paragraphToAdd);
        }
        paragraph.setSpacingAfter(spacingAfter);
    }

    public void addParagraph(String textoParrafo, float intSpacingBefore, float intSpacingAfter) {
        Paragraph paragraphToAdd = new Paragraph(textoParrafo, this.FONT_TEXT);
        paragraph.setSpacingBefore((float) intSpacingBefore);
        paragraph.add(paragraphToAdd);
        paragraph.setSpacingAfter((float) intSpacingAfter);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addParagraph: ", e.toString());
        }
    }

    public void addParagraph(String textoParrafo) {
        addParagraph(textoParrafo, 5, 5);
    }

    public void addImage(Image image) {
        try {
            document.add(image);
        } catch (DocumentException e) {
            Log.e("Error in addImage: ", e.toString());
        }
    }

    public void addImageDrawableAsBackgroud(int idResourceDrawable) {
        try {
            Drawable drawable = AppCompatResources.getDrawable(this.context, idResourceDrawable);
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                Image instance = Image.getInstance(byteArrayOutputStream.toByteArray());
                instance.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());
                instance.setAbsolutePosition(0.0f, 0.0f);
                addImage(instance);
            }
        } catch (BadElementException | IOException e) {
            Log.e("Error in addImageDrawableAsBackground: ", e.toString());
        }
    }

    public void createTable(String[] arrayEncabezados, ArrayList<String[]> arrayListFilas) {
        Paragraph paragraph2 = new Paragraph();
        paragraph = paragraph2;
        paragraph2.setFont(this.FONT_TEXT);
        PdfPTable pdfPTable = new PdfPTable(arrayEncabezados.length);
        pdfPTable.setWidthPercentage(100.0f);
        for (String phrase : arrayEncabezados) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(phrase, FONT_SUBTITLE));
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setBackgroundColor(BaseColor.GREEN);
            pdfPTable.addCell(pdfPCell);
        }
        for (int indiceFila = 0; indiceFila < arrayListFilas.size(); indiceFila++) {
            String[] valoresFila = arrayListFilas.get(indiceFila);
            for (String phrase2 : valoresFila) {
                PdfPCell pdfPCell = new PdfPCell(new Phrase(phrase2, FONT_TEXT));
                pdfPCell.setHorizontalAlignment(1);
                pdfPCell.setFixedHeight(40.0f);
                pdfPTable.addCell(pdfPCell);
            }
        }
        paragraph.add(pdfPTable);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTable: ", e.toString());
        }
    }

    public void addNewPage() {
        document.newPage();
    }

    public void attachPdfAndSendMail() {
        Intent intentShared = new Intent(Intent.ACTION_SEND);
        intentShared.setType("application/pdf");
        intentShared.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentShared.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentShared.putExtra(
                Intent.EXTRA_EMAIL,
                new String[]{
                        "sat@atlasromero.com",
                        "administracion@atlasromero.com",
                        "laguipemo@gmail.com"}
        );
        intentShared.putExtra(
                Intent.EXTRA_SUBJECT,
                "Informe del mantenimiento");
        intentShared.putExtra(
                Intent.EXTRA_TEXT,
                "Le adjuntamos el informe del mantenimiento realizado a su vitrina");

        Uri contentUri = FileProvider.getUriForFile(
                context,
                "net.iessanclemente.a19lazaropm.aservice.fileprovider",
                getPdfFile()
        );
        intentShared.putExtra(Intent.EXTRA_STREAM, contentUri);

        Intent chooser = Intent.createChooser(intentShared, "Seleccionar para enviar reporte");
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(
                    packageName,
                    contentUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        }
        activityResultLauncher.launch(chooser);
    }

    public void viewPdf() {
        Intent intent = new Intent(context, ViewPdfActivity.class);
        intent.putExtra("PATH", pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

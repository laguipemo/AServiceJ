package net.iessanclemente.a19lazaropm.aservice.reports;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.net.MailTo;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import net.iessanclemente.a19lazaropm.aservice.C1019R;

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

    private class PageBackgroundAtlas extends PdfPageEventHelper {
        private PageBackgroundAtlas() {
        }

        public void onStartPage(PdfWriter pdfWriter, Document document) {
            TemplatePdf.this.addImageDrawableAsBackgroud(C1019R.C1021drawable.fondo_informe_atlas);
        }
    }

    public TemplatePdf(Context context2) {
        this.context = context2;
        this.pdfsFolder = new File(context2.getExternalFilesDir(Environment.DIRECTORY_DCIM), "pdfs");
    }

    private void createFile(String str) {
        if (this.pdfsFolder.exists() || this.pdfsFolder.mkdirs()) {
            this.pdfFile = new File(this.pdfsFolder, str);
        } else {
            Toast.makeText(this.context, "No se pudo crear directorio", 0).show();
        }
    }

    public void openDocument(String str) {
        createFile(str);
        try {
            Document document2 = new Document(PageSize.f230A4);
            this.document = document2;
            this.pdfWriter = PdfWriter.getInstance(document2, new FileOutputStream(this.pdfFile));
            this.pdfWriter.setPageEvent(new PageBackgroundAtlas());
            this.document.open();
        } catch (Exception e) {
            Log.e("Error in openDocument: ", e.toString());
        }
    }

    public void openDocument() {
        openDocument("InformeVitrina.pdf");
    }

    public void openDocumetStamperPageNumering(String str) {
        createFile(str);
        try {
            PdfReader pdfReader = new PdfReader((InputStream) new FileInputStream(this.pdfFile));
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(this.pdfFile));
            int numberOfPages = pdfReader.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                PdfContentByte underContent = pdfStamper.getUnderContent(i);
                String format = String.format(Locale.getDefault(), "Pagina %d de %d", new Object[]{Integer.valueOf(i), Integer.valueOf(numberOfPages)});
                BaseFont createFont = BaseFont.createFont("Helvetica-Oblique", "Cp1252", false);
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
        this.document.close();
    }

    public File getPdfFile() {
        return this.pdfFile;
    }

    public void addMetaData(String str, String str2, String str3) {
        this.document.addTitle(str);
        this.document.addSubject(str2);
        this.document.addAuthor(str3);
    }

    public void addTitles(String str, String str2) {
        this.paragraph = new Paragraph();
        addChildParagraphCenter(new Paragraph(str, this.FONT_TITLE), 0.0f, 10.0f);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(20.0f);
        PdfPTable pdfPTable = new PdfPTable(1);
        pdfPTable.setWidthPercentage(50.0f);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(str2, this.FONT_TITLE));
        pdfPCell.setFixedHeight(40.0f);
        pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        pdfPCell.setHorizontalAlignment(1);
        pdfPCell.setVerticalAlignment(5);
        pdfPTable.addCell(pdfPCell);
        paragraph2.add((Element) pdfPTable);
        addChildParagraphCenter(paragraph2, 10.0f, 10.0f);
        this.paragraph.setSpacingAfter(30.0f);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addTitles: ", e.toString());
        }
    }

    public void addDatosEmpresa(String str, String str2, String str3, String str4, String str5, String str6) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(10.0f);
        paragraph2.add((Element) new Chunk("Fecha: ", this.FONT_TEXT_BOLD));
        paragraph2.add((Element) new Chunk(str, this.FONT_TEXT));
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.setSpacingBefore(10.0f);
        paragraph3.add((Element) new Chunk("Cliente: ", this.FONT_TEXT_BOLD));
        paragraph3.add((Element) new Chunk(str2, this.FONT_TEXT));
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.setSpacingBefore(10.0f);
        paragraph4.add((Element) new Chunk("Dirección: ", this.FONT_TEXT_BOLD));
        paragraph4.add((Element) new Chunk(str3, this.FONT_TEXT));
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.setSpacingBefore(10.0f);
        paragraph5.add((Element) new Chunk("Persona de contacto: ", this.FONT_TEXT_BOLD));
        paragraph5.add((Element) new Chunk(str4, this.FONT_TEXT));
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.setSpacingBefore(10.0f);
        paragraph6.add((Element) new Chunk("Telefono: ", this.FONT_TEXT_BOLD));
        paragraph6.add((Element) new Chunk(str5, this.FONT_TEXT));
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.setSpacingBefore(10.0f);
        paragraph7.add((Element) new Chunk("E-mail: ", this.FONT_TEXT_BOLD));
        paragraph7.add((Element) new Chunk(str6, this.FONT_TEXT));
        addChildParagraphLeft(paragraph7);
        this.paragraph.setSpacingBefore(50.0f);
        this.paragraph.setSpacingAfter(80.0f);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addDatosEmpresa: ", e.toString());
        }
    }

    public void addDatosVitrina(String str, String str2, String str3, String str4, String str5, String str6, boolean z, String str7, String str8) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Fabricante: ", this.FONT_TEXT_BOLD));
        paragraph2.add((Element) new Chunk(str, this.FONT_TEXT));
        paragraph2.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add((Element) new Chunk("Tipo: ", this.FONT_TEXT_BOLD));
        paragraph3.add((Element) new Chunk(str2, this.FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add((Element) new Chunk("Referencia: ", this.FONT_TEXT_BOLD));
        paragraph4.add((Element) new Chunk(str3, this.FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add((Element) new Chunk("Año construcción: ", this.FONT_TEXT_BOLD));
        paragraph5.add((Element) new Chunk(str4, this.FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add((Element) new Chunk("Nº de serie: ", this.FONT_TEXT_BOLD));
        paragraph6.add((Element) new Chunk(str5, this.FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add((Element) new Chunk("Nº de inventario: ", this.FONT_TEXT_BOLD));
        paragraph7.add((Element) new Chunk(str6, this.FONT_TEXT));
        paragraph7.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.add((Element) new Chunk("Puesta en marcha: ", this.FONT_TEXT_BOLD));
        paragraph8.add((Element) new Chunk(z ? "Si" : "No", this.FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add((Element) new Chunk("Servicio de mantenimiento: ", this.FONT_TEXT_BOLD));
        paragraph9.add((Element) new Chunk(str7, this.FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add((Element) new Chunk("Técnico responsable: ", this.FONT_TEXT_BOLD));
        paragraph10.add((Element) new Chunk(str8, this.FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addDatosVitrina: ", e.toString());
        }
    }

    public void addComprobAjusteSistemaExtracion(String str, String str2, boolean z, boolean z2, int i, float f) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Comprobación y ajuste del sistema de extracción", this.FONT_SUBTITLE));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(20.0f);
        addChildParagraphCenter(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add((Element) new Chunk("Funcionamiento del controlador digital: ", this.FONT_TEXT_BOLD));
        paragraph3.add((Element) new Chunk(str, this.FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add((Element) new Chunk("Inspección visual del sistema de extracción: ", this.FONT_TEXT_BOLD));
        paragraph4.add((Element) new Chunk(str2, this.FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add((Element) new Chunk("Requerimiento según norma DIN 12924: ", this.FONT_TEXT_BOLD));
        String str3 = "Si";
        paragraph5.add((Element) new Chunk(z ? str3 : "No", this.FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add((Element) new Chunk("Requerimiento según norma EN 14175: ", this.FONT_TEXT_BOLD));
        if (!z2) {
            str3 = "No";
        }
        paragraph6.add((Element) new Chunk(str3, this.FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add((Element) new Chunk("Medición de volumen de extracción", this.FONT_TEXT_BOLD_15));
        paragraph7.setSpacingBefore(30.0f);
        paragraph7.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.add((Element) new Chunk("Dimensiones de la Vitrina de Gases: ", this.FONT_TEXT_BOLD));
        paragraph8.add((Element) new Chunk(String.valueOf(i), this.FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add((Element) new Chunk("Longitud de la Guillotina: ", this.FONT_TEXT_BOLD));
        paragraph9.add((Element) new Chunk(String.valueOf(f), this.FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add((Element) new Chunk("Altura de trabajo: ", this.FONT_TEXT_BOLD));
        paragraph10.add((Element) new Chunk("500", this.FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComprobAjusteSistemaExtracion: ", e.toString());
        }
    }

    public void createTableMediciones(float[] fArr) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Velocidades en frontal (m/s)", this.FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph2);
        this.paragraph.setFont(this.FONT_TEXT);
        PdfPTable pdfPTable = new PdfPTable(fArr.length - 1);
        pdfPTable.setWidthPercentage(100.0f);
        for (int i = 1; i < fArr.length; i++) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), this.FONT_TEXT_BOLD));
            pdfPCell.setFixedHeight(25.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPTable.addCell(pdfPCell);
        }
        for (int i2 = 0; i2 < 1; i2++) {
            for (int i3 = 1; i3 < fArr.length; i3++) {
                PdfPCell pdfPCell2 = new PdfPCell(new Phrase(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(fArr[i3])}), this.FONT_TEXT));
                pdfPCell2.setFixedHeight(40.0f);
                pdfPCell2.setHorizontalAlignment(1);
                pdfPCell2.setVerticalAlignment(5);
                pdfPTable.addCell(pdfPCell2);
            }
        }
        this.paragraph.add((Element) pdfPTable);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add((Element) new Chunk("Velocidad media (m/s): ", this.FONT_TEXT_BOLD));
        paragraph3.add((Element) new Chunk(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(fArr[0])}), this.FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph3);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTableMediciones: ", e.toString());
        }
    }

    public void creatTableCalcVolExtraccion(float f, float f2, int i, int i2, int i3) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Cálculo del volumen de extracción", this.FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add((Element) new Chunk("Volumen de extracción (m³/h) = Longitud Gillotina (m) x Altura Trabajo (m) x Velocidad Aire (m/seg) x 3600 seg", this.FONT_TEXT_LITTLE_ITALIC));
        paragraph3.setSpacingAfter(10.0f);
        addChildParagraphLeft(paragraph3);
        this.paragraph.setFont(this.FONT_TEXT);
        String[] strArr = {"Longitud\nGillotina\n(m)", "Altura de\nTrabajo\n(m)", "Velocidad\ndel Aire\n(m/seg)", "Factor de\nconversión\n(seg)", "Volumen de\nextracción\n(m³/h)"};
        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100.0f);
        for (int i4 = 0; i4 < 5; i4++) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(strArr[i4], this.FONT_TEXT_BOLD));
            pdfPCell.setFixedHeight(60.0f);
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setVerticalAlignment(5);
            pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfPTable.addCell(pdfPCell);
        }
        Phrase[] phraseArr = {new Phrase(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f)}), this.FONT_TEXT), new Phrase(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(0.5f)}), this.FONT_TEXT), new Phrase(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f2)}), this.FONT_TEXT), new Phrase(String.format(Locale.getDefault(), TimeModel.NUMBER_FORMAT, new Object[]{3600}), this.FONT_TEXT), new Phrase(String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(f * 0.5f * f2 * 3600.0f)}), this.FONT_TEXT_BOLD_15)};
        for (int i5 = 0; i5 < 5; i5++) {
            PdfPCell pdfPCell2 = new PdfPCell(phraseArr[i5]);
            pdfPCell2.setFixedHeight(40.0f);
            pdfPCell2.setHorizontalAlignment(1);
            pdfPCell2.setVerticalAlignment(5);
            pdfPTable.addCell(pdfPCell2);
        }
        this.paragraph.add((Element) pdfPTable);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.setTabSettings(new TabSettings(40.0f));
        paragraph4.add((Element) new Chunk("Valores teóricos (m/s): ", this.FONT_TEXT_BOLD));
        paragraph4.add((Element) Chunk.TABBING);
        paragraph4.add((Element) new Chunk(String.format(Locale.getDefault(), "Min = %d ", new Object[]{Integer.valueOf(i)}), this.FONT_TEXT));
        paragraph4.add((Element) Chunk.TABBING);
        paragraph4.add((Element) new Chunk(String.format(Locale.getDefault(), "Máx = %d ", new Object[]{Integer.valueOf(i2)}), this.FONT_TEXT));
        paragraph4.add((Element) Chunk.TABBING);
        paragraph4.add((Element) new Chunk(String.format(Locale.getDefault(), "Recomendado = %d ", new Object[]{Integer.valueOf(i3)}), this.FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph4);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTableCalcVolExtraccion: ", e.toString());
        }
    }

    public void addComprobAjusteComponentesFijos(String str, String str2, String str3, String str4, String str5, float f, String str6, String str7, String str8, String str9) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Comprobación y ajuste de componentes mecánicos y medios (grifos/manoreductores)", this.FONT_SUBTITLE));
        paragraph2.setSpacingBefore(30.0f);
        paragraph2.setSpacingAfter(20.0f);
        addChildParagraphCenter(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.add((Element) new Chunk("Protección de la superficie: ", this.FONT_TEXT_BOLD));
        paragraph3.add((Element) new Chunk(str, this.FONT_TEXT));
        paragraph3.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add((Element) new Chunk("Aislamiento de las juntas en el interior: ", this.FONT_TEXT_BOLD));
        paragraph4.add((Element) new Chunk(str2, this.FONT_TEXT));
        paragraph4.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.add((Element) new Chunk("Sujeción correcta de las piezas fijas: ", this.FONT_TEXT_BOLD));
        paragraph5.add((Element) new Chunk(str3, this.FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.add((Element) new Chunk("Funcionamiento de la guillotina: ", this.FONT_TEXT_BOLD));
        paragraph6.add((Element) new Chunk(str4, this.FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        Paragraph paragraph7 = new Paragraph();
        paragraph7.add((Element) new Chunk("Estado general de la guillotina: ", this.FONT_TEXT_BOLD));
        paragraph7.add((Element) new Chunk(str5, this.FONT_TEXT));
        paragraph7.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph7);
        Paragraph paragraph8 = new Paragraph();
        paragraph8.setTabSettings(new TabSettings(20.0f));
        paragraph8.add((Element) new Chunk("Fuerza para el movimiento vertical de la guillotina: ", this.FONT_TEXT_BOLD));
        paragraph8.add((Element) new Chunk(String.format(Locale.getDefault(), "%.2f N ", new Object[]{Float.valueOf(f)}), this.FONT_TEXT));
        paragraph8.add((Element) Chunk.TABBING);
        paragraph8.add((Element) new Chunk(str6, this.FONT_TEXT));
        paragraph8.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph8);
        Paragraph paragraph9 = new Paragraph();
        paragraph9.add((Element) new Chunk("Control de presencia: ", this.FONT_TEXT_BOLD));
        paragraph9.add((Element) new Chunk(str7, this.FONT_TEXT));
        paragraph9.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph9);
        Paragraph paragraph10 = new Paragraph();
        paragraph10.add((Element) new Chunk("Autoproteccion: ", this.FONT_TEXT_BOLD));
        paragraph10.add((Element) new Chunk(str8, this.FONT_TEXT));
        paragraph10.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph10);
        Paragraph paragraph11 = new Paragraph();
        paragraph11.add((Element) new Chunk("Compobación del funcionamiento de grifos/monoreductores: ", this.FONT_TEXT_BOLD));
        paragraph11.add((Element) new Chunk(str9, this.FONT_TEXT));
        paragraph11.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph11);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComprobAjusteComponentesFijos: ", e.toString());
        }
    }

    public void addComentAndFinalEvalu(String str, boolean z, boolean z2) {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add((Element) new Chunk("Comentarios: ", this.FONT_TEXT_BOLD));
        paragraph2.setSpacingBefore(20.0f);
        addChildParagraphLeft(paragraph2);
        Paragraph paragraph3 = new Paragraph();
        paragraph3.setIndentationLeft(30.0f);
        paragraph3.add((Element) new Paragraph(str, this.FONT_TEXT));
        addChildParagraphLeft(paragraph3);
        Paragraph paragraph4 = new Paragraph();
        paragraph4.add((Element) new Chunk("Resultado: ", this.FONT_TEXT_BOLD));
        paragraph4.setSpacingBefore(30.0f);
        addChildParagraphLeft(paragraph4);
        Paragraph paragraph5 = new Paragraph();
        paragraph5.setIndentationLeft(30.0f);
        paragraph5.add((Element) new Chunk("El equipo está de acuerdo con las normas / regulaciones pertinentes: ", this.FONT_TEXT));
        String str2 = "Si";
        paragraph5.add((Element) new Chunk(z ? str2 : "No", this.FONT_TEXT));
        paragraph5.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph5);
        Paragraph paragraph6 = new Paragraph();
        paragraph6.setIndentationLeft(30.0f);
        paragraph6.add((Element) new Chunk("Por cuestiones de seguridad es necesaria la intervención del equipo: ", this.FONT_TEXT));
        if (!z2) {
            str2 = "No";
        }
        paragraph6.add((Element) new Chunk(str2, this.FONT_TEXT));
        paragraph6.setSpacingBefore(10.0f);
        addChildParagraphLeft(paragraph6);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addComentAndFinalEvalu: ", e.toString());
        }
    }

    public void addSignatures() {
        this.paragraph = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setSpacingBefore(80.0f);
        paragraph2.setIndentationLeft(30.0f);
        paragraph2.setTabSettings(new TabSettings(300.0f));
        paragraph2.add((Element) new Chunk("Firma Técnico", this.FONT_TEXT_BOLD));
        paragraph2.add((Element) Chunk.TABBING);
        paragraph2.add((Element) new Chunk("Firma de la propiedad", this.FONT_TEXT_BOLD));
        addChildParagraphLeft(paragraph2);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addSignatures: ", e.toString());
        }
    }

    public void addChildParagraphCenter(Paragraph paragraph2) {
        addChildParagraphCenter(paragraph2, 0.0f, 0.0f);
    }

    public void addChildParagraphCenter(Paragraph paragraph2, float f, float f2) {
        paragraph2.setAlignment(1);
        this.paragraph.setSpacingBefore(f);
        this.paragraph.add((Element) paragraph2);
        this.paragraph.setSpacingAfter(f2);
    }

    public void addChildParagraphLeft(Paragraph paragraph2) {
        addChildParagraphLeft(paragraph2, 0.0f, 0.0f);
    }

    public void addChildParagraphLeft(Paragraph paragraph2, float f, float f2) {
        paragraph2.setAlignment(0);
        this.paragraph.setSpacingBefore(f);
        this.paragraph.add((Element) paragraph2);
        this.paragraph.setSpacingAfter(f2);
    }

    public void addChildParagraphRight(Paragraph paragraph2) {
        addChildParagraphRight(paragraph2, 0.0f, 0.0f);
    }

    public void addChildParagraphRight(Paragraph paragraph2, float f, float f2) {
        paragraph2.setAlignment(2);
        this.paragraph.setSpacingBefore(f);
        this.paragraph.add((Element) paragraph2);
        this.paragraph.setSpacingAfter(f2);
    }

    public void addChildParagraphJustified(Paragraph paragraph2) {
        addChildParagraphJustified(paragraph2, 0.0f, 0.0f);
    }

    public void addChildParagraphJustified(Paragraph paragraph2, float f, float f2) {
        paragraph2.setAlignment(3);
        this.paragraph.setSpacingBefore(f);
        this.paragraph.add((Element) paragraph2);
        this.paragraph.setSpacingAfter(f2);
    }

    public void addChildParagraphAligned(Paragraph paragraph2, int i) {
        addChildParagraphAligned(paragraph2, i, 0.0f, 0.0f);
    }

    public void addChildParagraphAligned(Paragraph paragraph2, int i, float f, float f2) {
        this.paragraph.setSpacingBefore(f);
        if (i == 0) {
            addChildParagraphLeft(paragraph2);
        } else if (i == 1) {
            addChildParagraphCenter(paragraph2);
        } else if (i == 2) {
            addChildParagraphRight(paragraph2);
        } else if (i != 3) {
            paragraph2.setAlignment(i);
            this.paragraph.add((Element) paragraph2);
        } else {
            addChildParagraphJustified(paragraph2);
        }
        this.paragraph.setSpacingAfter(f2);
    }

    public void addParagraph(String str, int i, int i2) {
        Paragraph paragraph2 = new Paragraph(str, this.FONT_TEXT);
        this.paragraph = paragraph2;
        paragraph2.setSpacingBefore((float) i);
        this.paragraph.setSpacingAfter((float) i2);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in addParagraph: ", e.toString());
        }
    }

    public void addParagraph(String str) {
        addParagraph(str, 5, 5);
    }

    public void addImage(Image image) {
        try {
            this.document.add(image);
        } catch (DocumentException e) {
            Log.e("Error in addImage: ", e.toString());
        }
    }

    public void addImageDrawableAsBackgroud(int i) {
        try {
            Drawable drawable = AppCompatResources.getDrawable(this.context, i);
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                Image instance = Image.getInstance(byteArrayOutputStream.toByteArray());
                instance.scaleToFit(this.document.getPageSize().getWidth(), this.document.getPageSize().getHeight());
                instance.setAbsolutePosition(0.0f, 0.0f);
                addImage(instance);
            }
        } catch (BadElementException | IOException e) {
            Log.e("Error in addImageDrawableAsBackground: ", e.toString());
        }
    }

    public void createTable(String[] strArr, ArrayList<String[]> arrayList) {
        Paragraph paragraph2 = new Paragraph();
        this.paragraph = paragraph2;
        paragraph2.setFont(this.FONT_TEXT);
        PdfPTable pdfPTable = new PdfPTable(strArr.length);
        pdfPTable.setWidthPercentage(100.0f);
        for (String phrase : strArr) {
            PdfPCell pdfPCell = new PdfPCell(new Phrase(phrase, this.FONT_SUBTITLE));
            pdfPCell.setHorizontalAlignment(1);
            pdfPCell.setBackgroundColor(BaseColor.GREEN);
            pdfPTable.addCell(pdfPCell);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            String[] strArr2 = arrayList.get(i);
            for (String phrase2 : strArr2) {
                PdfPCell pdfPCell2 = new PdfPCell(new Phrase(phrase2, this.FONT_TEXT));
                pdfPCell2.setHorizontalAlignment(1);
                pdfPCell2.setFixedHeight(40.0f);
                pdfPTable.addCell(pdfPCell2);
            }
        }
        this.paragraph.add((Element) pdfPTable);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            Log.e("Error in createTable: ", e.toString());
        }
    }

    public void addNewPage() {
        this.document.newPage();
    }

    public void attachPdfAndSendMail() {
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"sat@atlasromero.com", "administracion@atlasromero.com", "laguipemo@me.com"});
        intent.putExtra("android.intent.extra.SUBJECT", "Informe del mantenimiento");
        intent.putExtra("android.intent.extra.TEXT", "Le adjuntamos el informe del mantenimiento realizado a su vitrina");
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(this.pdfFile));
        if (intent.resolveActivity(this.context.getPackageManager()) != null) {
            this.context.startActivity(intent);
        }
    }

    public void viewPdf() {
        Intent intent = new Intent(this.context, ViewPdfActivity.class);
        intent.putExtra("PATH", this.pdfFile.getAbsolutePath());
        intent.addFlags(268435456);
        this.context.startActivity(intent);
    }
}

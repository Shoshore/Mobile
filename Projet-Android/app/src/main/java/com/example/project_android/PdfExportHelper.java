package com.example.project_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfExportHelper {

    private static final int PAGE_WIDTH  = 595; // A4 en points
    private static final int PAGE_HEIGHT = 842;
    private static final int MARGIN      = 40;

    public static File generateParcoursPdf(Context context, ParcourModel parcours) throws IOException {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // ── Peintures ──────────────────────────────────────────────
        Paint paintBg = new Paint();
        paintBg.setColor(Color.parseColor("#1A1A2E"));

        Paint paintTitle = new Paint();
        paintTitle.setColor(Color.WHITE);
        paintTitle.setTextSize(22f);
        paintTitle.setFakeBoldText(true);

        Paint paintSubtitle = new Paint();
        paintSubtitle.setColor(Color.WHITE);
        paintSubtitle.setTextSize(13f);

        Paint paintSectionTitle = new Paint();
        paintSectionTitle.setColor(Color.parseColor("#1A1A2E"));
        paintSectionTitle.setTextSize(15f);
        paintSectionTitle.setFakeBoldText(true);

        Paint paintText = new Paint();
        paintText.setColor(Color.parseColor("#333333"));
        paintText.setTextSize(12f);

        Paint paintMeta = new Paint();
        paintMeta.setColor(Color.parseColor("#555555"));
        paintMeta.setTextSize(12f);

        Paint paintLine = new Paint();
        paintLine.setColor(Color.parseColor("#DDDDDD"));
        paintLine.setStrokeWidth(1f);

        Paint paintBadge = new Paint();
        paintBadge.setColor(Color.parseColor("#4CAF50"));

        Paint paintBadgeText = new Paint();
        paintBadgeText.setColor(Color.WHITE);
        paintBadgeText.setTextSize(11f);

        // ── Header ──────────────────────────────────────────────────
        canvas.drawRect(0, 0, PAGE_WIDTH, 90, paintBg);
        canvas.drawText("Traveling", MARGIN, 38f, paintTitle);
        canvas.drawText("Votre parcours personnalise", MARGIN, 58f, paintSubtitle);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
        canvas.drawText("Genere le " + date, MARGIN, 76f, paintSubtitle);

        // ── Titre parcours ──────────────────────────────────────────
        float y = 120f;
        canvas.drawText(parcours.getTitre(), MARGIN, y, paintSectionTitle);
        y += 20f;
        canvas.drawText("Ville : " + parcours.getVille(), MARGIN, y, paintMeta);
        y += 30f;

        // ── Metriques ───────────────────────────────────────────────
        // Badge budget
        canvas.drawRoundRect(MARGIN, y - 14f, MARGIN + 110f, y + 4f, 6f, 6f, paintBadge);
        canvas.drawText("Budget : " + parcours.getBudget() + " EUR", MARGIN + 6f, y, paintBadgeText);

        // Badge duree
        Paint paintBadge2 = new Paint(paintBadge);
        paintBadge2.setColor(Color.parseColor("#2196F3"));
        canvas.drawRoundRect(MARGIN + 118f, y - 14f, MARGIN + 228f, y + 4f, 6f, 6f, paintBadge2);
        canvas.drawText("Duree : " + parcours.getDuree() + "h", MARGIN + 124f, y, paintBadgeText);

        // Badge effort
        Paint paintBadge3 = new Paint(paintBadge);
        paintBadge3.setColor(Color.parseColor("#FF9800"));
        canvas.drawRoundRect(MARGIN + 236f, y - 14f, MARGIN + 346f, y + 4f, 6f, 6f, paintBadge3);
        canvas.drawText("Effort : " + parcours.getEffort(), MARGIN + 242f, y, paintBadgeText);

        y += 30f;
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, paintLine);
        y += 20f;

        // ── Etapes ──────────────────────────────────────────────────
        canvas.drawText("Etapes du parcours", MARGIN, y, paintSectionTitle);
        y += 20f;

        List<String> etapes = parcours.getEtapes();
        for (int i = 0; i < etapes.size(); i++) {
            // Numero etape
            Paint paintNum = new Paint();
            paintNum.setColor(Color.parseColor("#1A1A2E"));
            paintNum.setTextSize(11f);
            paintNum.setFakeBoldText(true);

            canvas.drawCircle(MARGIN + 8f, y - 4f, 10f, paintNum);
            Paint paintNumText = new Paint();
            paintNumText.setColor(Color.WHITE);
            paintNumText.setTextSize(10f);
            paintNumText.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(i + 1), MARGIN + 8f, y, paintNumText);

            canvas.drawText(etapes.get(i), MARGIN + 26f, y, paintText);
            y += 28f;

            // Ligne separatrice entre etapes
            if (i < etapes.size() - 1) {
                Paint paintDot = new Paint();
                paintDot.setColor(Color.parseColor("#EEEEEE"));
                paintDot.setStrokeWidth(1f);
                canvas.drawLine(MARGIN + 26f, y - 14f, PAGE_WIDTH - MARGIN, y - 14f, paintDot);
            }
        }

        y += 20f;
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, paintLine);
        y += 20f;

        // ── Conseils ────────────────────────────────────────────────
        canvas.drawText("Conseils", MARGIN, y, paintSectionTitle);
        y += 20f;

        String[] conseils = {
                "Verifiez les horaires d'ouverture avant de partir",
                "Prevoyez de la monnaie pour les petits commerces",
                "Telechargez les cartes hors-ligne avant votre depart",
                "Consultez la meteo la veille pour adapter votre tenue"
        };
        for (String conseil : conseils) {
            canvas.drawText("•  " + conseil, MARGIN, y, paintMeta);
            y += 20f;
        }

        y += 10f;
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, paintLine);
        y += 20f;

        // ── Footer ──────────────────────────────────────────────────
        Paint paintFooter = new Paint();
        paintFooter.setColor(Color.parseColor("#AAAAAA"));
        paintFooter.setTextSize(10f);
        paintFooter.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Genere par Traveling — application mobile de voyages",
                PAGE_WIDTH / 2f, PAGE_HEIGHT - 30f, paintFooter);

        document.finishPage(page);

        // ── Sauvegarde ──────────────────────────────────────────────
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (dir != null && !dir.exists()) dir.mkdirs();

        String fileName = "parcours_" + parcours.getVille().replaceAll(" ", "_")
                + "_" + System.currentTimeMillis() + ".pdf";
        File file = new File(dir, fileName);

        FileOutputStream fos = new FileOutputStream(file);
        document.writeTo(fos);
        document.close();
        fos.close();

        return file;
    }

    public static void openPdf(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                file
        );
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Ouvrir le PDF"));
    }
}
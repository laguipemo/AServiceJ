package net.iessanclemente.a19lazaropm.aservice.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;
import net.iessanclemente.a19lazaropm.aservice.R;

public class ViewPdfActivity extends AppCompatActivity {
    private static final int RESULT_SHOW_PROBLEM = 666;
    private File filePdf;
    private PDFView pdfView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_pdf);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator((int) R.drawable.ic_baseline_arrow_back_ios_24);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    private void init() {
        pdfView = findViewById(R.id.viewerPdf);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filePdf = new File(extras.getString("PATH", null));
        }
        if (filePdf == null || !filePdf.isFile()) {
            sendResult(filePdf, RESULT_SHOW_PROBLEM);
        } else {
            pdfView.fromFile(filePdf)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .spacing(5)
                    .load();
        }
    }

    private void sendResult(File file, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("FILE", file);
        setResult(resultCode, intent);
        finish();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

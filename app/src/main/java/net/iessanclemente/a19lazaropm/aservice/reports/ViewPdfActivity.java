package net.iessanclemente.a19lazaropm.aservice.reports;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;
import net.iessanclemente.a19lazaropm.aservice.C1019R;

public class ViewPdfActivity extends AppCompatActivity {
    private static final int RESULT_SHOW_PROBLEM = 666;
    private ActionBar actionBar;
    private File filePdf;
    private PDFView pdfView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C1019R.layout.activity_view_pdf);
        ActionBar supportActionBar = getSupportActionBar();
        this.actionBar = supportActionBar;
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator((int) C1019R.C1021drawable.ic_baseline_arrow_back_ios_24);
            this.actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    private void init() {
        this.pdfView = (PDFView) findViewById(C1019R.C1022id.viewerPdfPDFView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.filePdf = new File(extras.getString("PATH", (String) null));
        }
        File file = this.filePdf;
        if (file == null || !file.isFile()) {
            sendResult(this.filePdf, 666);
        } else {
            this.pdfView.fromFile(this.filePdf).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).spacing(5).load();
        }
    }

    private void sendResult(File file, int i) {
        Intent intent = new Intent();
        intent.putExtra("FILE", file);
        setResult(i, intent);
        finish();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

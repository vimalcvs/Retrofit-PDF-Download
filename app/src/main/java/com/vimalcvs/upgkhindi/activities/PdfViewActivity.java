package com.vimalcvs.upgkhindi.activities;

import static com.vimalcvs.upgkhindi.utils.Constant.TRUE;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.databinding.ActivityPdfBinding;
import com.vimalcvs.upgkhindi.models.Chapter;
import com.vimalcvs.upgkhindi.utils.Constant;
import com.vimalcvs.upgkhindi.utils.DefaultScroll;
import com.vimalcvs.upgkhindi.utils.Utils;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {

    private ProgressBar pdfViewProgressBar;
    private ActivityPdfBinding binding;
    private Chapter chapter;

    private String num2="";
    OnPageChangeListener onPageChangeListener = (i, i2) -> num2 = "/" + i2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ObjectAnimator.ofInt(binding.pdfViewProgressBar, "progress", 100).setDuration(5000).start();

        pdfViewProgressBar = binding.pdfViewProgressBar;
        pdfViewProgressBar.setVisibility(View.VISIBLE);

        chapter = (Chapter) getIntent().getSerializableExtra(Constant.EXTRA_OBJC);
        PdfView();
        setupToolbar();
    }

    private void setupToolbar() {
        try {
            final MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(TRUE);
                getSupportActionBar().setHomeButtonEnabled(TRUE);
                getSupportActionBar().setTitle(chapter.title);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void PdfView() {
        FileLoader.with(this)
                .load(Config.ADMIN_PANEL_URL + Constant.PDF_PATH + chapter.pdf, false)
                .fromDirectory("pdf", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        pdfViewProgressBar.setVisibility(View.GONE);
                        File pdfFile = response.getBody();
                        try {
                            binding.pdfView.fromFile(pdfFile)
                                    .defaultPage(0)
                                    .enableAnnotationRendering(true)
                                    .onLoad(PdfViewActivity.this)
                                    .onPageChange(onPageChangeListener)
                                    .scrollHandle(new DefaultScroll(PdfViewActivity.this){
                                        @Override
                                        public void setPageNum(int pageNum) {
                                            String text = String.valueOf(pageNum);
                                            if (!textView.getText().equals(text)) {
                                                String string =text+num2;
                                                textView.setText(string);
                                            }
                                        }
                                    })
                                    .spacing(10)
                                    .onPageError(PdfViewActivity.this)
                                    .load();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        pdfViewProgressBar.setVisibility(View.GONE);
                        Toast.makeText(PdfViewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void loadComplete(int nbPages) {
        pdfViewProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int page, Throwable t) {
        pdfViewProgressBar.setVisibility(View.GONE);
        Toast.makeText(PdfViewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
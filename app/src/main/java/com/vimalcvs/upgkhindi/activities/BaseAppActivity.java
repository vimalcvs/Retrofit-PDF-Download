package com.vimalcvs.upgkhindi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.utils.Utils;

public class BaseAppActivity extends AppCompatActivity {

    public Activity mActivity;
    private ProgressDialog progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    private void initProgressDialog() {
        try {
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    void showProgress() {
        showProgress(null);
    }

    private synchronized void showProgress(final String message) {
        if (progress == null) {
            initProgressDialog();
        }
        if (progress != null && !progress.isShowing()) {
            try {
                if (!Utils.isEmpty(message)) {
                    progress.setMessage(message);
                }
                progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progress.show();
                progress.setContentView(R.layout.dialog_progress);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        }
    }

    synchronized void hideProgress() {
        try {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }

    }
}

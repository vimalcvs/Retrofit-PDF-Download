package com.vimalcvs.upgkhindi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.github.barteksc.pdfviewer.util.Util;
import com.vimalcvs.upgkhindi.R;


public abstract class DefaultScroll extends RelativeLayout implements ScrollHandle {

    private final static int HANDLE_SHORT = 40;
    private final static int DEFAULT_TEXT_SIZE = 13;

    private float relativeHandlerMiddle = 0f;

    protected TextView textView;
    protected Context context;
    private final boolean inverted;
    private PDFView pdfView;
    private float currentPos;

    private final Handler handler = new Handler();
    private final Runnable hidePageScrollerRunnable = this::hide;

    public DefaultScroll(Context context) {
        this(context, false);
    }

    public DefaultScroll(Context context, boolean inverted) {
        super(context);
        this.context = context;
        this.inverted = inverted;
        textView = new TextView(context);
        setVisibility(INVISIBLE);
        setTextColor(Color.BLACK);
        setTextSize(DEFAULT_TEXT_SIZE);
    }

    @Override
    public void setupLayout(PDFView pdfView) {
        int align;
        Drawable background;
        if (pdfView.isSwipeVertical()) {
            if (inverted) { // left
                align = ALIGN_PARENT_LEFT;
            } else { // right
                align = ALIGN_PARENT_RIGHT;
            }
            background = ContextCompat.getDrawable(context, R.drawable.bg_scroll_handle_left);
        } else {
            if (inverted) { // top
                align = ALIGN_PARENT_TOP;
            } else { // bottom
                align = ALIGN_PARENT_BOTTOM;
            }
            background = ContextCompat.getDrawable(context, R.drawable.bg_scroll_handle_down);
        }

        setBackground(background);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 10, 20, 10);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(textView, layoutParams);

        lp.addRule(align);
        pdfView.addView(this, lp);

        this.pdfView = pdfView;
    }

    @Override
    public void destroyLayout() {
        pdfView.removeView(this);
    }

    @Override
    public void setScroll(float position) {
        if (!shown()) {
            show();
        } else {
            handler.removeCallbacks(hidePageScrollerRunnable);
        }
        if (pdfView != null) {
            setPosition((pdfView.isSwipeVertical() ? pdfView.getHeight() : pdfView.getWidth()) * position);
        }
    }

    private void setPosition(float pos) {
        if (Float.isInfinite(pos) || Float.isNaN(pos)) {
            return;
        }
        float pdfViewSize;
        if (pdfView.isSwipeVertical()) {
            pdfViewSize = pdfView.getHeight();
        } else {
            pdfViewSize = pdfView.getWidth();
        }
        pos -= relativeHandlerMiddle;

        if (pos < 0) {
            pos = 0;
        } else if (pos > pdfViewSize - Util.getDP(context, HANDLE_SHORT)) {
            pos = pdfViewSize - Util.getDP(context, HANDLE_SHORT);
        }

        if (pdfView.isSwipeVertical()) {
            setY(pos);
        } else {
            setX(pos);
        }

        calculateMiddle();
        invalidate();
    }

    private void calculateMiddle() {
        float pos, viewSize, pdfViewSize;
        if (pdfView.isSwipeVertical()) {
            pos = getY();
            viewSize = getHeight();
            pdfViewSize = pdfView.getHeight();
        } else {
            pos = getX();
            viewSize = getWidth();
            pdfViewSize = pdfView.getWidth();
        }
        relativeHandlerMiddle = ((pos + relativeHandlerMiddle) / pdfViewSize) * viewSize;
    }

    @Override
    public void hideDelayed() {
        handler.postDelayed(hidePageScrollerRunnable, 1000);
    }

    @Override
    public boolean shown() {
        return getVisibility() == VISIBLE;
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(INVISIBLE);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setTextSize(int size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    private boolean isPDFViewReady() {
        return pdfView != null && pdfView.getPageCount() > 0 && !pdfView.documentFitsView();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isPDFViewReady()) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                pdfView.stopFling();
                handler.removeCallbacks(hidePageScrollerRunnable);
                if (pdfView.isSwipeVertical()) {
                    currentPos = event.getRawY() - getY();
                } else {
                    currentPos = event.getRawX() - getX();
                }
            case MotionEvent.ACTION_MOVE:
                if (pdfView.isSwipeVertical()) {
                    setPosition(event.getRawY() - currentPos + relativeHandlerMiddle);
                    pdfView.setPositionOffset(relativeHandlerMiddle / (float) getHeight(), false);
                } else {
                    setPosition(event.getRawX() - currentPos + relativeHandlerMiddle);
                    pdfView.setPositionOffset(relativeHandlerMiddle / (float) getWidth(), false);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                hideDelayed();
                pdfView.performPageSnap();
                return true;
        }

        return super.onTouchEvent(event);
    }
}
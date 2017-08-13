package com.xinbob.circleprogressview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xinbob.circleprogressview.R;

/**
 * Created by xinbob on 8/13/17.
 * ring progress indicator view unclosed
 */

public class RingProgressView extends View {

    private Paint mPaint;

    private int x;
    private int y;

    private int spreadAngle = 0;
    private int percent = 0;
    private int factor = 0;

    private int mRadius;
    private int mRingThickness;

    private int mBarBackgroundColor;
    private int mRingColor;
    private int mProgressColor;


    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();

        // get from xml attr
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.RingProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RingProgressView_barRadius:
                    mRadius = a.getInteger(attr, 100);
                    break;

                case R.styleable.RingProgressView_ringThickness:
                    mRingThickness = a.getInteger(attr, 40);
                    break;

                case R.styleable.RingProgressView_barPrimaryColor:
                    mBarBackgroundColor = a.getColor(attr, Color.BLUE);
                    break;

                case R.styleable.RingProgressView_ringBackgroundColor:
                    mRingColor = a.getColor(attr, Color.RED);
                    break;

                case R.styleable.RingProgressView_ringProgressColor:
                    mProgressColor = a.getColor(attr, Color.GREEN);
                    break;
            }

        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            x = widthSize / 2;
        } else {
            width = mRadius * 2;
            x = mRadius;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
            y = heightSize / 2;
        } else {
            height = mRadius * 2;
            y = mRadius;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setColor(mBarBackgroundColor);
        canvas.drawCircle(x, y, mRadius, mPaint);

        // 环装进度指示器底色
        // 共270度 分100分 表示进度
        // 扇形展开起始角度 135度 展开划过270度
        mPaint.setColor(mRingColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingThickness);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        RectF rectF = new RectF(
                x - mRadius + mRingThickness / 2, y - mRadius + mRingThickness / 2,
                x + mRadius - mRingThickness / 2, y + mRadius - mRingThickness / 2
        );
        canvas.drawArc(rectF, 135, 270, false, mPaint);

        // 进度条
        mPaint.setColor(mProgressColor);
        canvas.drawArc(rectF, 135, spreadAngle, false, mPaint);

        mPaint.reset();

        // 画文字
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        String percentText = factor + "%";
        mPaint.setTextSize(80);

        Rect rect = new Rect();
        mPaint.getTextBounds(percentText, 0, percentText.length(), rect);
        canvas.drawText(percentText, x - rect.width() / 2, y + rect.height() / 2, mPaint);

        mPaint.setTextSize(40);
        String text = "使用率";
        Rect rr = new Rect();

        mPaint.getTextBounds(text, 0, text.length(), rr);
        canvas.drawText(text, x - rr.width() / 2, y + rr.height() / 2 + 135, mPaint);

        mPaint.reset();
    }

    public void setProgress(int percent) {
        if (percent > 0 && percent <= 100) {
            this.percent = percent;
            post(mSpreadAnimRunn);
        }
    }

    private Runnable mSpreadAnimRunn = new Runnable() {
        @Override
        public void run() {
            postInvalidate();

            factor++;
            spreadAngle = (int) (2.7 * factor);
            if (factor < percent) {
                postDelayed(mSpreadAnimRunn, 24);
            }
        }
    };


    public void resetUI() {
        postInvalidate();

        spreadAngle = 0;
        factor = 0;
        percent = 0;
    }

}

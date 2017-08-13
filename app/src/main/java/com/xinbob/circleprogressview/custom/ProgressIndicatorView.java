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
 * Created by xinbob on 8/12/17.
 * custom view
 */

public class ProgressIndicatorView extends View {

    private static final String TAG = "ProgressIndicatorView";

    private Paint mPaint;

    private int x;
    private int y;
    private int mRadius;
    private int mRingWidth;

    private int sweepAngle = 0;
    private int factor = 0;
    private int percent = 0;

    private int mPrimaryColor;
    private int mRingColor;


    public ProgressIndicatorView(Context context) {
        this(context, null);
    }

    public ProgressIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ProgressIndicatorView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ProgressIndicatorView_radius:
                    mRadius = a.getInteger(attr, 150);
                    break;

                case R.styleable.ProgressIndicatorView_ringWidth:
                    mRingWidth = a.getInteger(attr, 20);
                    break;

                case R.styleable.ProgressIndicatorView_primaryColor:
                    mPrimaryColor = a.getColor(attr, Color.BLUE);
                    break;

                case R.styleable.ProgressIndicatorView_ringColor:
                    mRingColor = a.getColor(attr, Color.RED);
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

        // 画圆
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPrimaryColor);
        canvas.drawCircle(x, y, mRadius, mPaint);

        // 画弧线
        mPaint.setColor(mRingColor);
        RectF rectF = new RectF(
                x - mRadius + mRingWidth / 2, y - mRadius + mRingWidth / 2,
                x + mRadius - mRingWidth / 2, y + mRadius - mRingWidth / 2
        );
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, 270, sweepAngle, false, mPaint);

        // 画文字
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        String percentText = factor + "%";
        mPaint.setTextSize(130);
        // 测量文字
        Rect textRect = new Rect();
        mPaint.getTextBounds(percentText, 0, percentText.length(), textRect);

        canvas.drawText(percentText, x - textRect.width() / 2, y + textRect.height() / 2, mPaint);
    }


    public void setProgressPercent(int percent) {
        if (percent > 0 && percent <= 100) {
            this.percent = percent;
            post(spreadAnim);
        }
    }

    private Runnable spreadAnim = new Runnable() {
        @Override
        public void run() {
            // invalidate(); 和postInvalidate有什么区别?
            postInvalidate();

            factor++;
            sweepAngle = (int) (3.6 * factor);
            if (factor < percent) {
                postDelayed(spreadAnim, 24);
            }
        }
    };

    public void resetUI() {
        postInvalidate();

        sweepAngle = 0;
        factor = 0;
        percent = 0;
    }
}

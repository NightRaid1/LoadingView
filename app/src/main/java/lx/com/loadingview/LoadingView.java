package lx.com.loadingview;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import lx.com.loadingview.R;

/**
 * Administrator on 2017/2/17 14:17
 */

public class LoadingView extends View {
    private static final String ONEANIM = "ONEANIM";
    private static final String TWOANIM = "TWOANIM";
    private static final String THREE = "THREE";

    private int oldWidth;
    private int oldHeight;
    private int animationTime = 1300;
    private int width;
    private int height;
    private int smallCircleRadius = 30;
    private int insideRectWidth = 10;
    private Paint mPaint;
    private int[] colors = new int[]{0xaaf00000, 0xaa00ffff, 0xccffaaff, 0xaafbbf00};
    private int oneStepValue;
    private int twoStepValue;
    private int threeStepValue;

    public LoadingView(Context context) {
        super(context);
        init(null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
            animationTime = typedArray.getInt(R.styleable.LoadingView_duration, animationTime);
        }
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(oldWidth / 2, oldHeight / 2);
        canvas.rotate(twoStepValue);
        for (int i = 0; i < colors.length; i++) {
            canvas.rotate(360 / colors.length * i);
            mPaint.setColor(colors[i]);
            canvas.drawCircle(0, -((width / 2 - insideRectWidth) / 2 + insideRectWidth) + oneStepValue, threeStepValue, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oldWidth = w;
        oldHeight = h;
        width = Math.min(w, h);
        height = width;
        smallCircleRadius = (height / 2 - insideRectWidth) / 2;
        threeStepValue = smallCircleRadius;
    }

    private void startAnimation() {
        PropertyValuesHolder oneAnimation = PropertyValuesHolder.ofInt(ONEANIM, 0, smallCircleRadius + insideRectWidth, 0);
        PropertyValuesHolder twoAnimation = PropertyValuesHolder.ofInt(TWOANIM, 0, 359);
        PropertyValuesHolder threeAnimation = PropertyValuesHolder.ofInt(THREE, smallCircleRadius, (int)(smallCircleRadius*0.7f), smallCircleRadius);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(oneAnimation, twoAnimation, threeAnimation).setDuration(animationTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                oneStepValue = (int) animation.getAnimatedValue(ONEANIM);
                twoStepValue = (int) animation.getAnimatedValue(TWOANIM);
                threeStepValue = (int) animation.getAnimatedValue(THREE);
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}

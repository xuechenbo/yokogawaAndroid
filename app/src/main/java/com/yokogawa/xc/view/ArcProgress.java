package com.yokogawa.xc.view;

/**
 * Description :
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.yokogawa.xc.BuildConfig;
import com.yokogawa.xc.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class ArcProgress extends View {
    private Paint paint;
    protected Paint textPaint;

    private RectF rectF = new RectF();


    private float strokeWidth;
    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;
    private String text;
    private float textSize;
    private int textColor;
    private int currentProgress = 0;
    private float progress = 0;
    private int max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;
    private Typeface typeFace;
    private float pointRoundWidth;
    private float pointInRoundWidth;
    private int fontResourceId;

    private float arcBottomHeight;

    private final int default_finished_color = Color.parseColor("#00BF5B");
    private final int yy = Color.parseColor("#E3DDDD");
    private final int default_unfinished_color = Color.parseColor("#F1F3F2");
    private int[] mColors = {Color.parseColor("#00BF5B"), Color.parseColor("#17D98B")};
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.8f;
    private float default_text_size;
    private final int min_size;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_SUFFIX = "suffix";
    private Paint CirPaint;

    public ArcProgress(Context context) {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = sp2px(getResources(), 18);
        min_size = (int) dp2px(getResources(), 100);
        default_text_size = sp2px(getResources(), 40);
        default_suffix_text_size = sp2px(getResources(), 15);
        default_suffix_padding = dp2px(getResources(), 4);
        default_suffix_text = "%";
        default_bottom_text_size = sp2px(getResources(), 10);
        default_stroke_width = dp2px(getResources(), 15);

        pointRoundWidth = dp2px(getResources(), 42);
        pointInRoundWidth = dp2px(getResources(), 30);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getFloat(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.ArcProgress_arc_suffix_text);
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
        initTypeFace(attributes);
    }

    private void initTypeFace(TypedArray attributes) {
        if (android.os.Build.VERSION.SDK_INT < 26) {
            int fontId = attributes.getResourceId(R.styleable.ArcProgress_arc_suffix_text_font, 0);
            if (fontId != 0) {
                try {
                    typeFace = ResourcesCompat.getFont(getContext(), fontId);
                } catch (Exception e) {
                    if (BuildConfig.DEBUG)
                        e.printStackTrace();
                }
            }
        } else {
            typeFace = attributes.getFont(R.styleable.ArcProgress_arc_suffix_text_font);
        }
    }


    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);


        CirPaint = new Paint();
        CirPaint.setColor(Color.WHITE);
        CirPaint.setAntiAlias(true);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        this.progress = Float.parseFloat(new DecimalFormat("#.##", dfs).format(progress));

        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        currentProgress = 0;
        invalidate();
    }


    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public String getText() {
        return text;
    }

    /**
     * Setting Central Text to custom String
     */
    public void setText(String text) {
        this.text = text;
        this.invalidate();
    }

    /**
     * Setting Central Text back to default one (value of the progress)
     */
    public void setDefaultText() {
        text = String.valueOf(getProgress());
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding() {
        return suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(pointRoundWidth / 2f, pointRoundWidth / 2f, width - pointRoundWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - pointRoundWidth / 2f);
//        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = currentProgress / (float) getMax() * arcAngle;
        float finishedStartAngle = startAngle;

        if (progress == 0) finishedStartAngle = 0.01f;
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);

        LinearGradient linearGradient = new LinearGradient(
                0, 0,
                rectF.right, rectF.top,
                Color.parseColor("#00BE72"),
                Color.parseColor("#17D98B"),
                Shader.TileMode.CLAMP
        );
        paint.setShader(linearGradient);
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);
        paint.setShader(null);
        float centerX = rectF.centerX();
        float centerY = rectF.centerY();
        float radius = rectF.width() / 2;
        CirPaint.setShadowLayer(50f, 0, 0, yy);
        canvas.drawCircle(centerX, centerY, radius - dp2px(getResources(), 20) * 2, CirPaint);

        int radiusLittle = (int) pointRoundWidth / 2; //圆环的半径
        int radiusInLittle = (int) pointInRoundWidth / 2; //圆环的半径
        float angle = (startAngle + finishedSweepAngle) % 360;
        float pointX = (float) (centerX + radius * Math.cos(angle * Math.PI / 180));
        float pointY = (float) (centerY + radius * Math.sin(angle * Math.PI / 180));
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(3, 0, 6, Color.parseColor("#26000000"));
        canvas.drawCircle(pointX, pointY, radiusLittle, paint); //画出圆环
        paint.setColor(finishedStrokeColor);
        paint.clearShadowLayer();
        canvas.drawCircle(pointX, pointY, radiusInLittle, paint); //画出圆环
//        Log.e("打印log", "运动的点：" + startAngle + "   " + finishedSweepAngle + "    " + arcAngle + "    " + angle + "   " + pointX + "   " + pointY);
//        textPaint.setTextSize(bottomTextSize);
//        bottomText = "b";
        if (currentProgress < progress) {
            currentProgress++;
            invalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}


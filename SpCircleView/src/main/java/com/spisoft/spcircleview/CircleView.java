package com.spisoft.spcircleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.content.res.AppCompatResources;

public class CircleView extends View {

    private static int DEFAULT_TITLE_COLOR = Color.CYAN;
    private static int DEFAULT_SUBTITLE_COLOR = Color.WHITE;

    private static String DEFAULT_TITLE = "";
    private static String DEFAULT_SUBTITLE = "";

    private static boolean DEFAULT_SHOW_TITLE = true;
    private static boolean DEFAULT_SHOW_SUBTITLE = true;

    private static float DEFAULT_TITLE_SIZE = 25f;
    private static float DEFAULT_SUBTITLE_SIZE = 20f;
    private static float DEFAULT_TITLE_SUBTITLE_SPACE = 0f;
    private static float DEFAULT_ICON_MARGIN = 0f;

    private static int DEFAULT_STROKE_COLOR = Color.CYAN;
    private static int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static int DEFAULT_FILL_COLOR = Color.DKGRAY;

    private static float DEFAULT_STROKE_WIDTH = 5f;
    private static float DEFAULT_FILL_RADIUS = 0.9f;

    private static final int DEFAULT_VIEW_SIZE = 96;

    private int mTitleColor = DEFAULT_TITLE_COLOR;
    private int mSubtitleColor = DEFAULT_SUBTITLE_COLOR;
    private int mStrokeColor = DEFAULT_STROKE_COLOR;
    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mFillColor = DEFAULT_FILL_COLOR;

    private String mTitleText = DEFAULT_TITLE;
    private String mSubtitleText = DEFAULT_SUBTITLE;

    private float mTitleSize = DEFAULT_TITLE_SIZE;
    private float mSubtitleSize = DEFAULT_SUBTITLE_SIZE;
    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;
    private float mFillRadius = DEFAULT_FILL_RADIUS;
    private float mTitleSubtitleSpace = DEFAULT_TITLE_SUBTITLE_SPACE;

    private boolean mShowTitle = DEFAULT_SHOW_TITLE;
    private boolean mShowSubtitle = DEFAULT_SHOW_SUBTITLE;

    private TextPaint mTitleTextPaint;
    private TextPaint mSubTextPaint;

    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private Paint mFillPaint;
    private Drawable mDrawable = null;
    private float mDrawableMargin;

    private RectF mInnerRectF;

    private int mViewSize;
    private Context mContext;

    public CircleView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleView, defStyle, 0);

        if(a.hasValue(R.styleable.CircleView_cv_titleText)){
            mTitleText = a.getString(R.styleable.CircleView_cv_titleText);
        }

        if(a.hasValue(R.styleable.CircleView_cv_subtitleText)){
            mSubtitleText = a.getString(R.styleable.CircleView_cv_subtitleText);
        }

        mContext = context;
        mTitleColor = a.getColor(R.styleable.CircleView_cv_titleColor,DEFAULT_TITLE_COLOR);
        mSubtitleColor = a.getColor(R.styleable.CircleView_cv_subtitleColor,DEFAULT_SUBTITLE_COLOR);
        mBackgroundColor = a.getColor(R.styleable.CircleView_cv_backgroundColorValue,DEFAULT_BACKGROUND_COLOR);
        mStrokeColor = a.getColor(R.styleable.CircleView_cv_strokeColorValue,DEFAULT_STROKE_COLOR);
        mFillColor = a.getColor(R.styleable.CircleView_cv_fillColor,DEFAULT_FILL_COLOR);

        mTitleSize = a.getDimension(R.styleable.CircleView_cv_titleSize,DEFAULT_TITLE_SIZE);
        mSubtitleSize = a.getDimension(R.styleable.CircleView_cv_subtitleSize,DEFAULT_SUBTITLE_SIZE);

        mStrokeWidth = a.getFloat(R.styleable.CircleView_cv_strokeWidthSize,DEFAULT_STROKE_WIDTH);
        mFillRadius = a.getFloat(R.styleable.CircleView_cv_fillRadius,DEFAULT_FILL_RADIUS);

        mTitleSubtitleSpace = a.getFloat(R.styleable.CircleView_cv_titleSubtitleSpace, DEFAULT_TITLE_SUBTITLE_SPACE);
        mViewSize = a.getInteger(R.styleable.CircleView_cv_size, DEFAULT_VIEW_SIZE);

        int drawableResId = a.getResourceId(R.styleable.CircleView_cv_icon, -1);
        if(drawableResId >= 0)
            mDrawable = AppCompatResources.getDrawable(context, drawableResId);
        mDrawableMargin = a.getDimension(R.styleable.CircleView_cv_icon_margin, DEFAULT_ICON_MARGIN);

        a.recycle();

        //Title TextPaint
        mTitleTextPaint = new TextPaint();
        mTitleTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTitleTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTitleTextPaint.setTextAlign(Paint.Align.CENTER);
        mTitleTextPaint.setLinearText(true);
        mTitleTextPaint.setColor(mTitleColor);
        mTitleTextPaint.setTextSize(mTitleSize);

        //Subtitle TextPaint
        mSubTextPaint = new TextPaint();
        mSubTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mSubTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mSubTextPaint.setTextAlign(Paint.Align.CENTER);
        mSubTextPaint.setLinearText(true);
        mSubTextPaint.setColor(mSubtitleColor);
        mSubTextPaint.setTextSize(mSubtitleSize);

        //Stroke Paint
        mStrokePaint = new Paint();
        mStrokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStrokeWidth(mStrokeWidth);

        //Background Paint
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);


        //Fill Paint
        mFillPaint = new Paint();
        mFillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(mFillColor);

        mInnerRectF = new RectF();

//        ImageView imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.ic_baseline_add_reaction_24);
//
//        LayoutParams layoutParams = new RelativeLayout.LayoutParams(mViewSize, mViewSize);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        this.addView(imageView, layoutParams);

    }

    private void invalidateTextPaints(){
        mTitleTextPaint.setColor(mTitleColor);
        mSubTextPaint.setColor(mSubtitleColor);
        mTitleTextPaint.setTextSize(mTitleSize);
        mSubTextPaint.setTextSize(mSubtitleSize);
        invalidate();
    }

    private void invalidatePaints(){
        mBackgroundPaint.setColor(mBackgroundColor);
        mStrokePaint.setColor(mStrokeColor);
        mFillPaint.setColor(mFillColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = resolveSize(DEFAULT_VIEW_SIZE, widthMeasureSpec);
        int height = resolveSize(DEFAULT_VIEW_SIZE, heightMeasureSpec);
        mViewSize = Math.min(width, height);

        setMeasuredDimension(width, height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        mInnerRectF.set(0, 0, mViewSize, mViewSize);
        mInnerRectF.offset((getWidth() - mViewSize) / 2, (getHeight() - mViewSize) / 2);

        final int halfBorder = (int) (mStrokePaint.getStrokeWidth() / 2f + 0.5f);

        mInnerRectF.inset(halfBorder, halfBorder);

        float centerX = mInnerRectF.centerX();
        float centerY = mInnerRectF.centerY();

        canvas.drawArc(mInnerRectF, 0, 360, true, mBackgroundPaint);

        float radius = (mViewSize / 2) * mFillRadius;

        canvas.drawCircle(centerX, centerY, radius + 0.5f - mStrokePaint.getStrokeWidth(), mFillPaint);

        int xPos = (int) centerX;
        int yPos = (int) (centerY - (mTitleTextPaint.descent() + mTitleTextPaint.ascent()) / 2);

        canvas.drawOval(mInnerRectF, mStrokePaint);

        if (mShowTitle) {
            canvas.drawText(mTitleText,
                    xPos,
                    yPos,
                    mTitleTextPaint);
        }

        if (mShowSubtitle) {
            canvas.drawText(mSubtitleText,
                    xPos,
                    yPos + 20 + mTitleSubtitleSpace,
                    mSubTextPaint);
        }

        if (mDrawable != null) {
            int left = (int) (0 + mDrawableMargin);
            int top = (int) (0 + mDrawableMargin);
            int right = (int) (this.getMeasuredWidth() - mDrawableMargin);
            int bottom = (int) (this.getMeasuredHeight() - mDrawableMargin);
            mDrawable.setBounds(left, top, right, bottom);
            mDrawable.draw(canvas);
        }
    }

    public void setAlpha(int alpha){
//        mDrawable.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        mDrawable.setAlpha(alpha);
        invalidate();
    }

    /**
     * Sets whether the view's title string will be shown.
     * @param flag The boolean value.
     */
    public void setShowTitle(boolean flag){
        this.mShowTitle = flag;
        invalidate();
    }

    /**
     * Sets whether the view's subtitle string will be shown.
     * @param flag The boolean value.
     */
    public void setShowSubtitle(boolean flag){
        this.mShowSubtitle = flag;
        invalidate();
    }

    /**
     * Gets the title string attribute value.
     * @return The title string attribute value.
     */
    public String getTitleText() {
        return mTitleText;
    }

    /**
     * Sets the view's title string attribute value.
     * @param title The example string attribute value to use.
     */
    public void setTitleText(String title) {
        mTitleText = title;
        invalidate();
    }

    /**
     * Gets the subtitle string attribute value.
     * @return The subtitle string attribute value.
     */
    public String getSubtitleText() {
        return mSubtitleText;
    }

    /**
     * Sets the view's subtitle string attribute value.
     * @param subtitle The example string attribute value to use.
     */
    public void setSubtitleText(String subtitle) {
        mSubtitleText = subtitle;
        invalidate();
    }

    /**
     * Gets the stroke color attribute value.
     * @return The stroke color attribute value.
     */
    public int getStrokeColor() {
        return mStrokeColor;
    }

    /**
     * Gets the background color attribute value.
     * @return The background color attribute value.
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Gets the fill color attribute value.
     * @return The fill color attribute value.
     */
    public int getFillColor() {
        return mStrokeColor;
    }

    /**
     * Sets the view's stroke color attribute value.
     * @param strokeColor The stroke color attribute value to use.
     */
    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        invalidatePaints();
    }

    /**
     * Sets the view's background color attribute value.
     * @param backgroundColor The background color attribute value to use.
     */
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidatePaints();
    }

    /**
     * Sets the view's fill color attribute value.
     * @param fillColor The fill color attribute value to use.
     */
    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
        invalidatePaints();
    }

    /**
     * Gets the stroke width dimension attribute value.
     * @return The stroke width dimension attribute value.
     */
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * Sets the view's stroke width attribute value.
     * @param strokeWidth The stroke width attribute value to use.
     */
    public void setBackgroundColor(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    /**
     * Gets the fill radius dimension attribute value.
     * @return The fill radius dimension attribute value.
     */
    public float getFillRadius() {
        return mFillRadius;
    }

    /**
     * Sets the view's fill radius attribute value.
     * @param fillRadius The fill radius attribute value to use.
     */
    public void setFillRadius(float fillRadius) {
        mFillRadius = fillRadius;
        invalidate();
    }

    /**
     * Gets the title size dimension attribute value.
     * @return The title size dimension attribute value.
     */
    public float getTitleSize() {
        return mTitleSize;
    }

    /**
     * Sets the view's title size dimension attribute value.
     * @param titleSize The title size dimension attribute value to use.
     */
    public void setTitleSize(float titleSize) {
        mTitleSize = titleSize;
        invalidateTextPaints();
    }

    /**
     * Gets the subtitle size dimension attribute value.
     * @return The subtitle size dimension attribute value.
     */
    public float getSubtitleSize() {
        return mSubtitleSize;
    }

    /**
     * Sets the view's subtitle size dimension attribute value.
     * @param subtitleSize The subtitle size dimension attribute value to use.
     */
    public void setSubtitleSize(float subtitleSize) {
        mSubtitleSize = subtitleSize;
        invalidateTextPaints();
    }

    /**
     * Gets the title text color attribute value.
     * @return The text color attribute value.
     */
    public int getTitleColor() {
        return mTitleColor;
    }

    /**
     * Sets the view's title text color attribute value.
     * @param titleColor The title text color attribute value to use.
     */
    public void setTitleColor(int titleColor) {
        mTitleColor =titleColor;
        invalidateTextPaints();
    }

    /**
     * Gets the subtitle text color attribute value.
     * @return The text color attribute value.
     */
    public int getSubtitleColor() {
        return mSubtitleColor;
    }

    /**
     * Sets the view's subtitle text color attribute value.
     * @param subtitleColor The subtitle text color attribute value to use.
     */
    public void setSubtitleColor(int subtitleColor) {
        mSubtitleColor = subtitleColor;
        invalidateTextPaints();
    }

    /**
     * Gets the title subtitle space attribute value.
     * @return The title subtitle space attribute value.
     */
    public float getTitleSubtitleSpace() {
        return mTitleSubtitleSpace;
    }

    /**
     * Sets the view's title subtitle space attribute value.
     * @param titleSubtitleSpace The space between title and subtitle attribute value to use.
     */
    public void setTitleSubtitleSpace(float titleSubtitleSpace) {
        this.mTitleSubtitleSpace = titleSubtitleSpace;
        invalidateTextPaints();
    }

    public void setViewSize(int viewSize){
        this.mViewSize = viewSize;
        invalidateTextPaints();
    }

    public void setIcon(Drawable icon){
        this.mDrawable = icon;
        invalidateTextPaints();
    }
}

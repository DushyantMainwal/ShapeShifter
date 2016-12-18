package com.dushyant.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Dushyant on 12/16/2016.
 *          Modified by Bunk3r on 12/17/2016
 */
public class PolygonView extends View {
    private static final String TAG = "PolygonView";

    private static final int FIT_XY = 0;
    private static final int CENTER_CROP = 1;

    @Retention(SOURCE)
    @IntDef(value = {FIT_XY, CENTER_CROP})
    private @interface ScaleType {}

    private final Paint srcPaint = new Paint();
    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final RectF viewBounds = new RectF();
    private final RectF scaleRect = new RectF();

    @Nullable
    private Bitmap bitmap;

    @ScaleType
    private int scaleType = FIT_XY;

    @ColorInt
    private int borderColor = Color.TRANSPARENT;

    @IntRange(from = 3)
    private int sides = 3;

    @IntRange(from = 0,
              to = 360)
    private int rotateDegree = 0;

    @FloatRange(from = 1.0f)
    private float borderWidth = 3.0f;

    @DrawableRes
    private int imageResource = 0;

    @FloatRange(from = 0.0f)
    private float screenWidth;

    @FloatRange(from = 0.0f)
    private float screenHeight;

    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        if (!isInEditMode()) {
            TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.PolygonView);

            borderColor = typedArray.getColor(R.styleable.PolygonView_borderColor, borderColor);
            borderWidth = typedArray.getDimension(R.styleable.PolygonView_borderWidth, borderWidth);

            sides = typedArray.getInt(R.styleable.PolygonView_sides, sides);
            if (sides < 3) {
                throw new IllegalStateException("You can't have less than 3 sides to form a polygon");
            }

            imageResource = typedArray.getResourceId(R.styleable.PolygonView_src, imageResource);
            rotateDegree = typedArray.getInt(R.styleable.PolygonView_rotation, rotateDegree);

            //noinspection ResourceType
            scaleType = typedArray.getInt(R.styleable.PolygonView_scaleType, scaleType);

            typedArray.recycle();

            if (imageResource != 0) {
                try {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
                } catch (OutOfMemoryError error) {
                    bitmap = null;
                    Log.e(TAG, "Image is too large " + error.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            // This is for the preview mode
            borderColor = Color.BLACK;
            sides = 6;
            bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.GRAY);
        }

        rotateDegree += 90;

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
    }

    private void setupConfig() {
        // In case is set to {@code CENTER_CROP} we need to configure the bounds differently
        if (scaleType == CENTER_CROP) {
            setupCenterCrop();
        }

        // Settings the bounds for the view and for {@code CENTER_CROP}
        viewBounds.set(0, 0, screenWidth, screenHeight);

        // Settings the color of the border
        paint.setColor(borderColor);

        // Switches from degrees to radians
        float rotateRadian = (float) (rotateDegree * (Math.PI / 180));

        // Resets the previous path
        path.reset();

        //minimum value to set polygon
        float r = Math.min(screenHeight, screenWidth);

        // Calculates first point
        float xCorPoly = (float) ((getMeasuredWidth() / 2) + (r / 2) * Math.cos(2 * Math.PI * 0 / sides - rotateRadian));//90deg= 1.5708rad
        float yCorPoly = (float) ((getMeasuredHeight() / 2) + (r / 2) * Math.sin(2 * Math.PI * 0 / sides - rotateRadian));

        // Sets the start of the path to the first point
        path.moveTo(xCorPoly, yCorPoly);

        for (int i = 1; i < sides; i++) {
            xCorPoly = (float) ((getMeasuredWidth() / 2) + (r / 2) * Math.cos(2 * Math.PI * i / sides - rotateRadian));//90deg= 1.5708rad
            yCorPoly = (float) ((getMeasuredHeight() / 2) + (r / 2) * Math.sin(2 * Math.PI * i / sides - rotateRadian));

            path.lineTo(xCorPoly, yCorPoly);

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Point " + i + " (" + xCorPoly + "," + yCorPoly + ")");
            }
        }

        // Finishes the path
        path.close();
    }

    private void setupCenterCrop() {
        if (bitmap != null) {
            float ratioChange = 1;
            final int bitmapWidth = bitmap.getWidth();
            final int bitmapHeight = bitmap.getHeight();

            if (screenWidth != bitmapWidth) {
                ratioChange = screenWidth / bitmapWidth;
            }

            if (ratioChange * bitmapHeight < screenHeight) {
                ratioChange = screenHeight / bitmapHeight;
            }

            final float requiredWidth = bitmapWidth * ratioChange;
            final float requiredHeight = bitmapHeight * ratioChange;
            float x = (int) ((requiredWidth / 2) - (screenWidth / 2));
            float y = (int) ((requiredHeight / 2) - (screenHeight / 2));

            x = x <= 0 ? x : -x;
            y = y <= 0 ? y : -y;

            scaleRect.set(x, y, x + requiredWidth, y + requiredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            setupConfig();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.clipPath(path);
        if (bitmap != null) {
            if (scaleType == CENTER_CROP) {
                canvas.clipRect(scaleRect);
                canvas.drawBitmap(bitmap, null, scaleRect, srcPaint);
            } else {
                canvas.drawBitmap(bitmap, null, viewBounds, srcPaint);
            }
        }

        canvas.drawPath(path, paint);
    }
}
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Dushyant on 12/16/2016.
 */

public class PolygonView extends View {

    private ScaleType scaleType;
    private int borderColor;
    private Paint paint;
    private Path path;
    private int sides;
    private int rotateDegree;
    private float rotateRadian;
    private int imageResource;
    private Bitmap bitmap;
    private Float xCorPoly[];
    private Float yCorPoly[];
    private RectF viewBounds, scaleRect;
    private boolean border;
    private float borderWidth;

    private float screenWidth;
    private float screenHeight;
    private float requiredWidth;
    private float requiredHeight;
    private int x;
    private int y;

    public enum ScaleType {
        CENTRE_CROP(0),
        FIT_XY(1);
        final int value;

        ScaleType(int value) {
            this.value = value;
        }
    }

    private static final ScaleType[] scaleTypeArray = {ScaleType.CENTRE_CROP, ScaleType.FIT_XY};

    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void sides(int sides) {
        this.sides = sides;
    }

    private void init(Context context, AttributeSet attr) {
//        scaleType = ScaleType.CENTRE_CROP;//default
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.PolygonView);
            borderColor = typedArray.getColor(R.styleable.PolygonView_borderColor, 0);
            sides = typedArray.getInt(R.styleable.PolygonView_sides, 3);
            imageResource = typedArray.getResourceId(R.styleable.PolygonView_src, 0);
            borderWidth = typedArray.getDimension(R.styleable.PolygonView_borderWidth, 3.0f);
            rotateDegree = typedArray.getInt(R.styleable.PolygonView_rotation, 0);
            border = typedArray.getBoolean(R.styleable.PolygonView_border, false);
            scaleType = scaleTypeArray[typedArray.getInt(R.styleable.PolygonView_scaleType, 0)];
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        rotateDegree += 90;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        path = new Path();
        viewBounds = new RectF();
        scaleRect = new RectF();
        if (imageResource != 0) {
            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
            } catch (OutOfMemoryError error) {
                bitmap = null;
                Log.e("Image Error: ", "Image is too large " + error.getMessage());
            } catch (Exception e) {
                Log.e("Image Error: ", e.getMessage());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        if (bitmap != null && scaleType == ScaleType.CENTRE_CROP) {
            float ratioChange = 1;
            if (screenWidth != bitmap.getWidth()) {
                ratioChange = screenWidth / bitmap.getWidth();
            }
            if (ratioChange * bitmap.getHeight() < screenHeight) {
                ratioChange = screenHeight / bitmap.getHeight();
            }
            requiredHeight = bitmap.getHeight() * ratioChange;
            requiredWidth = bitmap.getWidth() * ratioChange;
            y = (int) ((requiredHeight / 2) - (screenHeight / 2));
            x = (int) ((requiredWidth / 2) - (screenWidth / 2));
            if (x > 0) x = -x;
            if (y > 0) y = -y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        xCorPoly = new Float[sides];
        yCorPoly = new Float[sides];

        float r = Math.min(screenHeight, screenWidth);//minimum value to set polygon
        viewBounds.set(0, 0, screenWidth, screenHeight);

        if (borderColor != 0) {
            paint.setColor(borderColor);
        } else paint.setColor(Color.BLACK);

        rotateRadian = (float) (rotateDegree * (Math.PI / 180));
        for (int i = 0; i < sides; i++) {
            xCorPoly[i] = (float) ((getMeasuredWidth() / 2) + (r / 2) * Math.cos(2 * Math.PI * i / sides - rotateRadian));//90deg= 1.5708rad
            yCorPoly[i] = (float) ((getMeasuredHeight() / 2) + (r / 2) * Math.sin(2 * Math.PI * i / sides - rotateRadian));
            System.out.print("Point " + i + " (" + xCorPoly[i] + "," + yCorPoly[i] + ") \n");
        }

        path.moveTo(xCorPoly[0], yCorPoly[0]);
        for (int i = 1; i < sides; i++) {
            path.lineTo(xCorPoly[i], yCorPoly[i]);
        }
        path.close();
        canvas.clipPath(path);
        if (bitmap != null) {
            if (scaleType == ScaleType.CENTRE_CROP) {
                scaleRect.set(x, y, x + requiredWidth, y + requiredHeight);
                canvas.clipRect(scaleRect);
                canvas.drawBitmap(bitmap, null, scaleRect, paint);
            } else {
                canvas.drawBitmap(bitmap, null, viewBounds, paint);
            }
            canvas.drawPath(path, paint);
        }
        if (border)
            canvas.drawPath(path, paint);
    }
}
package com.bkmsx.cropvideo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;

/**
 * Created by bkmsx on 2/14/2017.
 */

public class CropFrame extends AppCompatImageView {
    int TOP_LEFT = 1, BOTTOM_RIGHT = 2, TOP_RIGHT = 3, BOTTOM_LEFT = 4,
            CENTER = 5, OUT = 0;
    int SLOP = 100;

    int xMin, xMax, yMin, yMax,
        x1, x2, y1, y2;
    Paint paint;
    Path path;
    Context context;
    float xDown, yDown;
    int touchPoint = OUT;

    CropFrame(Context context) {super(context);}

    public CropFrame(Context context, Point[] points) {
        super(context);
        this.context = context;
        xMin = points[0].x;
        yMin = points[0].y;
        xMax = points[1].x;
        yMax = points[1].y;
        x1 = points[2].x;
        y1 = points[2].y;
        x2 = points[3].x;
        y2 = points[3].y;
        paint = new Paint();
        path = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                if (xDown >= x1 - SLOP && xDown <= x1 + SLOP
                        && yDown >= y1 - SLOP && yDown <= y1 + SLOP) {
                    touchPoint = TOP_LEFT;
                } else if (xDown >= x2 - SLOP && xDown <= x2 + SLOP
                        && yDown >= y2 - SLOP && yDown <= y2 + SLOP) {
                    touchPoint = BOTTOM_RIGHT;
                } else if (xDown >= x2 - SLOP && xDown <= x2 + SLOP
                        && yDown >= y1 - SLOP && yDown <= y1 + SLOP) {
                    touchPoint = TOP_RIGHT;
                } else if (xDown >= x1 - SLOP && xDown <= x1 + SLOP
                        && yDown >= y2 - SLOP && yDown <= y2 + SLOP) {
                    touchPoint = BOTTOM_LEFT;
                } else if (xDown >= x1 + SLOP && xDown <= x2 - SLOP
                        && yDown >= y1 + SLOP && yDown <= y2 - SLOP) {
                    touchPoint = CENTER;
                } else {
                    touchPoint = OUT;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchPoint == OUT) {
                    return true;
                }
                float xMove = event.getX();
                float yMove = event.getY();
                float xDelta = event.getX() - xDown;
                float yDelta = event.getY() - yDown;

                if (touchPoint == TOP_LEFT) {
                    x1 += xDelta;
                    y1 += yDelta;
                    if (x1 > x2 - SLOP) {
                        x1 = x2 - SLOP;
                    }
                    if (y1 > y2 - SLOP) {
                        y1 = y2 - SLOP;
                    }
                    if (x1 < xMin) {
                        x1 = xMin;
                    }
                    if (y1 < yMin) {
                        y1 = yMin;
                    }

                } else if (touchPoint == BOTTOM_RIGHT) {
                    x2 += xDelta;
                    y2 += yDelta;
                    if (x2 < x1 + SLOP) {
                        x2 = x1 + SLOP;
                    }
                    if (y2 < y1 + SLOP) {
                        y2 = y1 + SLOP;
                    }
                    if (x2 > xMax) {
                        x2 = xMax;
                    }
                    if (y2 > yMax) {
                        y2 = yMax;
                    }
                } else if (touchPoint == TOP_RIGHT) {
                    x2 += xDelta;
                    y1 += yDelta;
                    if (x2 < x1 + SLOP) {
                        x2 = x1 + SLOP;
                    }
                    if (x2 > xMax) {
                        x2 = xMax;
                    }
                    if (y1 > y2 - SLOP) {
                        y1 = y2 - SLOP;
                    }
                    if (y1 < yMin) {
                        y1 = yMin;
                    }
                } else if (touchPoint == BOTTOM_LEFT) {
                    x1 += xDelta;
                    y2 += yDelta;
                    if (x1 > x2 - SLOP) {
                        x1 = x2 - SLOP;
                    }
                    if (x1 < xMin) {
                        x1 = xMin;
                    }
                    if (y2 < y1 + SLOP) {
                        y2 = y1 + SLOP;
                    }
                    if (y1 > yMax) {
                        y1 = yMax;
                    }
                } else {
                    int width = x2 - x1;
                    int height = y2 - y1;
                    x1 += xDelta;
                    x2 += xDelta;
                    y1 += yDelta;
                    y2 += yDelta;

                    if (x1 < xMin) {
                        x1 = xMin;
                        x2 = x1 + width;
                    }
                    if (x2 > xMax) {
                        x2 = xMax;
                        x1 = x2 - width;
                    }
                    if (y1 < yMin) {
                        y1 = yMin;
                        y2 = y1 + height;
                    }
                    if (y2 > yMax) {
                        y2 = yMax;
                        y1 = y2 - height;
                    }
                }

                xDown = xMove;
                yDown = yMove;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchPoint = OUT;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.reset();
        path.reset();

        //draw crop frame
        paint.setColor(ContextCompat.getColor(context, R.color.overlay_color));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawRectangle(xMin, yMin, xMax, yMax);
        drawRectangle(x1, y1, x2, y2);
        path.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(path, paint);

        // draw grid and frame stroke
        paint.setStrokeWidth(4);
        int strokeColorId = touchPoint == CENTER ? R.color.crop_highlight_color : R.color.frame_stroke;
        paint.setColor(ContextCompat.getColor(context, strokeColorId));
        drawFrameStroke(canvas);
        drawGrid(canvas);

        // draw thumbs
        drawThumbs(canvas);
    }

    private void drawThumbs(Canvas canvas) {
        int thumbLength = 50;
        int bigWidth = 18;
        int smallWidth = 8;
        int highLightColor = ContextCompat.getColor(context, R.color.crop_highlight_color);
        //thumb TOP_LEFT
        int colorId = touchPoint == TOP_LEFT ?  highLightColor: Color.WHITE;
        paint.setColor(colorId);
        int thumbWidth = touchPoint == TOP_LEFT ? bigWidth : smallWidth;
        paint.setStrokeWidth(thumbWidth);
        canvas.drawLine(x1, y1 - thumbWidth / 2, x1, y1 + thumbLength, paint);
        canvas.drawLine(x1, y1, x1 + thumbLength, y1, paint);
        // thumb TOP_RIGHT
        colorId = touchPoint == TOP_RIGHT ?  highLightColor: Color.WHITE;
        paint.setColor(colorId);
        thumbWidth = touchPoint == TOP_RIGHT ? bigWidth : smallWidth;
        paint.setStrokeWidth(thumbWidth);
        canvas.drawLine(x2, y1 - thumbWidth / 2, x2, y1 + thumbLength, paint);
        canvas.drawLine(x2, y1, x2 - thumbLength, y1, paint);
        // thumb BOTTOM_RIGHT
        colorId = touchPoint == BOTTOM_RIGHT ?  highLightColor: Color.WHITE;
        paint.setColor(colorId);
        thumbWidth = touchPoint == BOTTOM_RIGHT ? bigWidth : smallWidth;
        paint.setStrokeWidth(thumbWidth);
        canvas.drawLine(x2, y2 + thumbWidth / 2, x2, y2 - thumbLength, paint);
        canvas.drawLine(x2, y2, x2 - thumbLength, y2, paint);
        // thumb BOTTOM_LEFT
        colorId = touchPoint == BOTTOM_LEFT ?  highLightColor: Color.WHITE;
        paint.setColor(colorId);
        thumbWidth = touchPoint == BOTTOM_LEFT ? bigWidth : smallWidth;
        paint.setStrokeWidth(thumbWidth);
        canvas.drawLine(x1, y2 + thumbWidth / 2, x1, y2 - thumbLength, paint);
        canvas.drawLine(x1, y2, x1 + thumbLength, y2, paint);
    }

    private void drawGrid(Canvas canvas) {
        int gridNum = 2;
        int gridWidth = (x2 - x1) / (gridNum + 1);
        int gridHeight = (y2 - y1) / (gridNum + 1);
        for (int i = 1; i <= gridNum; i++) {
            canvas.drawLine(x1, y1 + gridHeight * i, x2, y1 + gridHeight * i, paint);
            canvas.drawLine(x1 + gridWidth * i, y1, x1 + gridWidth * i, y2, paint);
        }
    }

    private void drawFrameStroke(Canvas canvas) {
        canvas.drawLine(x1, y1, x1, y2, paint);
        canvas.drawLine(x1, y2, x2, y2, paint);
        canvas.drawLine(x2, y2, x2, y1, paint);
        canvas.drawLine(x2, y1, x1, y1, paint);
    }

    private void drawRectangle(int x1, int y1, int x2, int y2) {
        path.moveTo(x1, y1);
        path.lineTo(x1, y2);
        path.lineTo(x2, y2);
        path.lineTo(x2, y1);
        path.close();
    }
}

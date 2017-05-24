package com.example.fmoyader.becomepainter.canvas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import com.example.fmoyader.becomepainter.canvas.dto.Point;

/**
 * Created by fmoyader on 24/5/17.
 */

public class DrawingView extends View {

    private static final float TOLERANCE_MIN_DISTANCE = 4;
    private Point controlPoint;

    private Context context;
    private Path path;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    public DrawingView(Context context) {
        super(context);
        this.context = context;

        path = new Path();
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStrokeWidth(5.0f);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point point = new Point(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartAt(point);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMoveAt(point);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchFinishAt(point);
                invalidate();
                break;
        }

        return true;
    }

    private void touchFinishAt(Point point) {
        path.lineTo(point.getX(), point.getY());
        // commit the path to our offscreen
        canvas.drawPath(path, paint);
        // kill this so we don't double draw
        //path.reset();
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        //mPaint.setMaskFilter(null);
    }

    private void touchMoveAt(Point newPoint) {
        float xIncrement = Math.abs(controlPoint.getX() - newPoint.getX());
        float yIncrement = Math.abs(controlPoint.getY() - newPoint.getY());

        // Pythagorean theorem
        double distance = Math.sqrt(Math.pow(xIncrement, 2) + Math.pow(yIncrement, 2));
        if (distance > TOLERANCE_MIN_DISTANCE) {
            path.lineTo(controlPoint.getX(), controlPoint.getY());
            // commit the path to our offscreen
            canvas.drawPath(path, paint);

            controlPoint = newPoint;
        }
    }

    private void touchStartAt(Point point) {
        controlPoint = point;
        path.reset();
        path.moveTo(point.getX(), point.getY());
    }
}

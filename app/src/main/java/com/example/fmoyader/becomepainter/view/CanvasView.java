package com.example.fmoyader.becomepainter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.fmoyader.becomepainter.dto.Line;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.dto.Point;

/**
 * Created by fmoyader on 24/5/17.
 */

public class CanvasView extends View {

    private static final float TOLERANCE_MIN_DISTANCE = 4;

    private Context context;
    private Paint paint;
    private Painting painting;
    private Line line;
    private boolean newPainting;

    private OnDrawingListener onDrawingListener;

    public void setOnDrawingListener(OnDrawingListener onDrawingListener) {
        this.onDrawingListener = onDrawingListener;
    }

    public interface OnDrawingListener {
        void onNewPaintingStarted();
    }

    public CanvasView(Context context) {
        super(context);
        initialize(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public void initialize(Context context) {
        this.context = context;

        newPainting = true;
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStrokeWidth(5.0f);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!newPainting) {
            painting.drawLineInCanvas(context, line, canvas);
        } else {
            painting = new Painting();
            newPainting = false;

            if (onDrawingListener != null) {
                onDrawingListener.onNewPaintingStarted();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point point = new Point(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartAt(point);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMoveAt(point);
                break;
            case MotionEvent.ACTION_UP:
                touchFinishAt(point);
                break;
        }

        return true;
    }

    private void touchFinishAt(Point point) {
        line.moveToPoint(point);
        invalidate();
    }

    private void touchMoveAt(Point newPoint) {
        Point controlPoint = line.getLastPoint();
        float xIncrement = Math.abs(controlPoint.getX() - newPoint.getX());
        float yIncrement = Math.abs(controlPoint.getY() - newPoint.getY());

        // Pythagorean theorem
        double distance = Math.sqrt(Math.pow(xIncrement, 2) + Math.pow(yIncrement, 2));
        if (distance > TOLERANCE_MIN_DISTANCE) {
            line.moveToPoint(newPoint);
            invalidate();
        }
    }

    private void touchStartAt(Point point) {
        line = new Line(paint);
        line.moveToPoint(point);
    }

    public Painting getPainting() {
        return painting;
    }

    public void reset() {
        newPainting = true;
        invalidate();
    }
}

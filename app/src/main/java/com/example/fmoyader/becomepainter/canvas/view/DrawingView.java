package com.example.fmoyader.becomepainter.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.fmoyader.becomepainter.canvas.dto.Line;
import com.example.fmoyader.becomepainter.canvas.dto.Painting;
import com.example.fmoyader.becomepainter.canvas.dto.Point;

/**
 * Created by fmoyader on 24/5/17.
 */

public class DrawingView extends View {

    private static final float TOLERANCE_MIN_DISTANCE = 4;

    private Context context;
    private Paint paint;
    private Painting painting;
    private Line line;

    public DrawingView(Context context) {
        super(context);
        this.context = context;
        painting = new Painting();

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStrokeWidth(5.0f);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (line != null) {
            line.drawInCanvas(canvas);
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

        painting.saveLine(line);
        //paint = new Paint();
        //line = new Line(paint);
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
}

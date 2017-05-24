package com.example.fmoyader.becomepainter.canvas.dto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Line {
    List<Point> points;
    private Paint paint;

    public Line(Paint paint) {
        this.paint = paint;
        points = new ArrayList<>();
    }

    public void moveToPoint(Point point) {
        points.add(point);
    }

    public Point getLastPoint() {
        return points.get(points.size() - 1);
    }

    public void drawInCanvas(Canvas canvas) {
        if (!points.isEmpty()) {
            Point start = points.get(0);
            for (int i = 1; i < points.size(); i++) {
                Point end = points.get(i);
                canvas.drawLine(
                        start.getX(), start.getY(),
                        end.getX(), end.getY(),
                        paint
                );
                start = end;
            }
        }
    }
}

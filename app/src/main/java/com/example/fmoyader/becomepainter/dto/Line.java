package com.example.fmoyader.becomepainter.dto;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Line implements Parcelable{
    List<Point> points;
    private Paint paint;

    private int colorId;
    private long id;

    private long paintingId;

    public Line() {
        this(Color.BLACK);
    }

    public Line(int colorId) {
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setStrokeWidth(5.0f);
        paint.setColor(colorId);
    }

    public Line(Paint paint) {
        this.paint = paint;
        points = new ArrayList<>();
    }

    protected Line(Parcel in) {
        points = in.createTypedArrayList(Point.CREATOR);
        colorId = in.readInt();
        paintingId = in.readLong();
        id = in.readLong();
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

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

    public long getPaintingId() {
        return paintingId;
    }

    public void setPaintingId(long paintingId) {
        this.paintingId = paintingId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(points);
        dest.writeInt(colorId);
        dest.writeLong(paintingId);
        dest.writeLong(id);
    }

    public boolean isStored() {
        return this.id > 0;
    }
}

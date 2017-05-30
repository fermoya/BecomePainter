package com.example.fmoyader.becomepainter.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Point implements Parcelable {
    private float x;
    private float y;
    private long lineId;
    private long id;

    public Point() {
        this(0,0);
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    protected Point(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        lineId = in.readLong();
        id = in.readLong();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeLong(lineId);
        dest.writeLong(id);
    }

    public boolean isStored() {
        return this.id > 0;
    }
}

package com.example.fmoyader.becomepainter.dto;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.fmoyader.becomepainter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Painting implements Parcelable {

    private List<Line> lines;
    private String title;
    private String description;
    private String author;
    private long id;

    public Painting() {
        lines = new ArrayList<>();
    }

    protected Painting(Parcel in) {
        lines = in.createTypedArrayList(Line.CREATOR);
        title = in.readString();
        description = in.readString();
        author = in.readString();
        id = in.readLong();
    }

    public static final Creator<Painting> CREATOR = new Creator<Painting>() {
        @Override
        public Painting createFromParcel(Parcel in) {
            return new Painting(in);
        }

        @Override
        public Painting[] newArray(int size) {
            return new Painting[size];
        }
    };

    public void drawLineInCanvas(Context context, Line newLine, Canvas canvas) {
        if (newLine != null) {
            if (!lines.contains(newLine)) {
                Log.d(context.getString(R.string.tag_debug_drawing), "Added new line");
                lines.add(newLine);
            }

            for (Line line : lines) {
                line.drawInCanvas(canvas);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean isBlank() {
        return lines.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(lines);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeLong(id);
    }

    public boolean isStored() {
        return this.id > 0;
    }

    public boolean isValid() {
        return author != null && !author.isEmpty()
                && title != null && !title.isEmpty();
    }

    public int numberOfLines() {
        return lines.size();
    }
}

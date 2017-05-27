package com.example.fmoyader.becomepainter.canvas.dto;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 24/5/17.
 */

public class Painting {

    private List<Line> lines;
    private String title;
    private String description;
    private String author;
    private long id;

    public Painting() {
        lines = new ArrayList<>();
    }

    public void drawLineInCanvas(Line newLine, Canvas canvas) {
        if (newLine != null) {
            if (!lines.contains(newLine)) {
                Log.i("LINE TEST", "Added new line");
                lines.add(newLine);
            }

            for (Line line : lines) {
                line.drawInCanvas(canvas);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBlank() {
        return lines.isEmpty();
    }
}

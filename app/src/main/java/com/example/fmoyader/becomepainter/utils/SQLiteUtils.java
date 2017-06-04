package com.example.fmoyader.becomepainter.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.fmoyader.becomepainter.dto.Line;
import com.example.fmoyader.becomepainter.dto.Painting;
import com.example.fmoyader.becomepainter.dto.Point;
import com.example.fmoyader.becomepainter.sqlite.contract.LineContract.LineEntry;
import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract.PaintingEntry;
import com.example.fmoyader.becomepainter.sqlite.contract.PointContract.PointEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmoyader on 27/5/17.
 */

public class SQLiteUtils {

    public static void persistInDatabase(Painting painting, Context context) {
        ContentValues values = new ContentValues();
        values.put(PaintingEntry.COLUMN_TITLE, painting.getTitle());
        values.put(PaintingEntry.COLUMN_AUTHOR, painting.getAuthor());
        values.put(PaintingEntry.COLUMN_DESCRIPTION, painting.getDescription());

        String id;
        if (!painting.isStored()) {
            Uri resultUri = context.getContentResolver().insert(PaintingEntry.CONTENT_URI, values);
            id = resultUri.getLastPathSegment();
        } else {
            id = painting.getId() + "";
            Uri requestUri = PaintingEntry.CONTENT_URI.buildUpon().appendPath(id).build();
            context.getContentResolver().update(
                    requestUri, values, null, null);
        }

        painting.setId(Long.parseLong(id));
        for (Line line : painting.getLines()) {
            line.setPaintingId(Long.parseLong(id));
            persistInDatabase(line, context);
        }

    }

    public static void persistInDatabase(Line line, Context context) {
        ContentValues values = new ContentValues();
        values.put(LineEntry.COLUMN_PAINTING_ID, line.getPaintingId());
        values.put(LineEntry.COLUMN_COLOR, line.getColorId());

        String id;
        if (!line.isStored()) {
            Uri resultUri = context.getContentResolver().insert(LineEntry.CONTENT_URI, values);
            id = resultUri.getLastPathSegment();
        } else {
            id = line.getId() + "";
            Uri requestUri = LineEntry.CONTENT_URI.buildUpon().appendPath(id).build();
            context.getContentResolver().update(
                    requestUri, values, null, null);
        }

        line.setId(Long.parseLong(id));
        for (Point point : line.getPoints()) {
            point.setLineId(Long.parseLong(id));
            persistInDatabase(point, context);
        }
    }

    public static void persistInDatabase(Point point, Context context) {
        ContentValues values = new ContentValues();
        values.put(PointEntry.COLUMN_LINE_ID, point.getLineId());
        values.put(PointEntry.COLUMN_X, point.getX());
        values.put(PointEntry.COLUMN_Y, point.getY());

        String id;
        if (!point.isStored()) {
            Uri resultUri = context.getContentResolver().insert(PointEntry.CONTENT_URI, values);
            id = resultUri.getLastPathSegment();
        } else {
            id = point.getId() + "";
            Uri requestUri = PointEntry.CONTENT_URI.buildUpon().appendPath(id).build();
            context.getContentResolver().update(
                    requestUri, values, null, null);
        }

        point.setId(Long.parseLong(id));
    }

    public static List<Painting> fetchAllPaintingSummaries(Context context) {
        List<Painting> paintings = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                PaintingEntry.CONTENT_URI,
                null, null, null, "_ID DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Painting painting = mapCursor2Painting(cursor);
                paintings.add(painting);
            }

            cursor.close();
        }

        return paintings;
    }

    public static Painting fetchPaintingById(String paintingId, Context context) {
        Painting painting = null;
        Cursor cursor = context.getContentResolver().query(
                PaintingEntry.CONTENT_URI.buildUpon().appendPath(paintingId).build(),
                null, null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                painting = mapCursor2Painting(cursor);
                List<Line> lines = fetchAllLinesInPainting(context, painting.getId());
                painting.setLines(lines);
            }
        }

        return painting;
    }

    public static List<Painting> fetchAllPaintings(Context context) {
        List<Painting> paintings = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                PaintingEntry.CONTENT_URI,
                null, null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Painting painting = mapCursor2Painting(cursor);
                List<Line> lines = fetchAllLinesInPainting(context, painting.getId());
                painting.setLines(lines);
                paintings.add(painting);
            }

            cursor.close();
        }

        return paintings;
    }

    private static List<Line> fetchAllLinesInPainting(Context context, long paintingId) {
        List<Line> lines = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                LineEntry.CONTENT_URI, null, LineEntry.COLUMN_PAINTING_ID + " = ?",
                new String[]{paintingId + ""}, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Line line = mapCursor2Line(cursor);
                List<Point> points = fetchAllPointsInLine(context, line.getId());
                line.setPoints(points);
                lines.add(line);
            }

            cursor.close();
        }

        return lines;
    }

    private static List<Point> fetchAllPointsInLine(Context context, long lineId) {
        List<Point> points = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                PointEntry.CONTENT_URI, null, PointEntry.COLUMN_LINE_ID + " = ?",
                new String[]{lineId + ""}, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Point point = mapCursor2Point(cursor);
                points.add(point);
            }

            cursor.close();
        }
        return points;
    }

    public static Point mapCursor2Point(Cursor cursor) {
        Point point = new Point();
        point.setId(cursor.getLong(cursor.getColumnIndex(PointEntry._ID)));
        point.setLineId(cursor.getLong(cursor.getColumnIndex(PointEntry.COLUMN_LINE_ID)));
        point.setX(cursor.getFloat(cursor.getColumnIndex(PointEntry.COLUMN_X)));
        point.setY(cursor.getFloat(cursor.getColumnIndex(PointEntry.COLUMN_Y)));
        return point;
    }

    public static Line mapCursor2Line(Cursor cursor) {
        Line line = new Line();
        line.setId(cursor.getLong(cursor.getColumnIndex(LineEntry._ID)));
        line.setPaintingId(cursor.getLong(cursor.getColumnIndex(LineEntry.COLUMN_PAINTING_ID)));
        line.setColorId(cursor.getInt(cursor.getColumnIndex(LineEntry.COLUMN_COLOR)));

        return line;
    }

    public static Painting mapCursor2Painting(Cursor cursor) {
        Painting painting = new Painting();
        painting.setId(cursor.getLong(cursor.getColumnIndex(PaintingEntry._ID)));
        painting.setTitle(cursor.getString(cursor.getColumnIndex(PaintingEntry.COLUMN_TITLE)));
        painting.setAuthor(cursor.getString(cursor.getColumnIndex(PaintingEntry.COLUMN_AUTHOR)));
        painting.setDescription(cursor.getString(cursor.getColumnIndex(PaintingEntry.COLUMN_DESCRIPTION)));
        return painting;
    }
}

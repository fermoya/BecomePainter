package com.example.fmoyader.becomepainter.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.fmoyader.becomepainter.canvas.dto.Line;
import com.example.fmoyader.becomepainter.canvas.dto.Painting;
import com.example.fmoyader.becomepainter.canvas.dto.Point;
import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract.PaintingEntry;
import com.example.fmoyader.becomepainter.sqlite.contract.LineContract.LineEntry;
import com.example.fmoyader.becomepainter.sqlite.contract.PointContract.PointEntry;

/**
 * Created by fmoyader on 27/5/17.
 */

public class SQLiteUtils {

    public static void persistInDatabase(Painting painting, Context context) {
        ContentValues values = new ContentValues();
        values.put(PaintingEntry.COLUMN_TITLE, painting.getTitle());
        values.put(PaintingEntry.COLUMN_AUTHOR, painting.getAuthor());
        values.put(PaintingEntry.COLUMN_DESCRIPTION, painting.getDescription());

        Uri uri = context.getContentResolver().insert(PaintingEntry.CONTENT_URI, values);
        String id = uri.getLastPathSegment();

        for (Line line : painting.getLines()) {
            line.setPaintingId(Long.parseLong(id));
            persistInDatabase(line, context);
        }
    }

    public static void persistInDatabase(Line line, Context context) {
        ContentValues values = new ContentValues();
        values.put(LineEntry.COLUMN_PAINTING_ID, line.getPaintingId());
        values.put(LineEntry.COLUMN_COLOR, line.getColorId());

        Uri uri = context.getContentResolver().insert(LineEntry.CONTENT_URI, values);
        String id = uri.getLastPathSegment();

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

        context.getContentResolver().insert(PointEntry.CONTENT_URI, values);
    }
}

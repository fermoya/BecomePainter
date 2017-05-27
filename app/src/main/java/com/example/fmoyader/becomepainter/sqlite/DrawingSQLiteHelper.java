package com.example.fmoyader.becomepainter.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fmoyader.becomepainter.sqlite.contract.LineContract;
import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract;
import com.example.fmoyader.becomepainter.sqlite.contract.PointContract;

/**
 * Created by fmoyader on 26/5/17.
 */

public class DrawingSQLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "drawing_canvas.db";

    public DrawingSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            String createPointsTableQuery = "CREATE TABLE " + PointContract.PointEntry.TABLE_NAME + "("
                    + PointContract.PointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PointContract.PointEntry.COLUMN_X + " REAL NOT NULL, "
                    + PointContract.PointEntry.COLUMN_Y + " REAL NOT NULL,"
                    + PointContract.PointEntry.COLUMN_LINE_ID + " INTEGER NOT NULL);";

            String createLinesTableQuery = "CREATE TABLE " + LineContract.LineEntry.TABLE_NAME + "("
                    + LineContract.LineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + LineContract.LineEntry.COLUMN_COLOR + " INTEGER NOT NULL,"
                    + LineContract.LineEntry.COLUMN_PAINTING_ID + " INTEGER NOT NULL);";

            String createPaintingsTableQuery = "CREATE TABLE " + PaintingContract.PaintingEntry.TABLE_NAME + "("
                    + PaintingContract.PaintingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PaintingContract.PaintingEntry.COLUMN_TITLE + " TEXT NOT NULL,"
                    + PaintingContract.PaintingEntry.COLUMN_DESCRIPTION + " TEXT NULL,"
                    + PaintingContract.PaintingEntry.COLUMN_AUTHOR + " TEXT NOT NULL);";

            db.execSQL(createPointsTableQuery);
            db.execSQL(createLinesTableQuery);
            db.execSQL(createPaintingsTableQuery);
        }
    }
}

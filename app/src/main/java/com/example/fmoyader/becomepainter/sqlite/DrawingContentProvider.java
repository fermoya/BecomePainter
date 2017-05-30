package com.example.fmoyader.becomepainter.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.fmoyader.becomepainter.R;
import com.example.fmoyader.becomepainter.sqlite.contract.LineContract;
import com.example.fmoyader.becomepainter.sqlite.contract.PaintingContract;
import com.example.fmoyader.becomepainter.sqlite.contract.PointContract;

/**
 * Created by fmoyader on 26/5/17.
 */

public class DrawingContentProvider extends ContentProvider {

    private DrawingSQLiteHelper drawingSQLiteHelper;

    private static UriMatcher uriMatcher = buildUriMatcher();

    private static final int POINTS = 100;
    private static final int POINTS_WITH_ID = 101;
    private static final int LINES = 200;
    private static final int LINES_WITH_ID = 201;
    private static final int PAINTING = 300;
    private static final int PAINTING_WITH_ID = 301;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PointContract.AUTHORITY, PointContract.POINT_PATH, POINTS);
        uriMatcher.addURI(PointContract.AUTHORITY, PointContract.POINT_PATH + "/#", POINTS_WITH_ID);
        uriMatcher.addURI(LineContract.AUTHORITY, LineContract.LINE_PATH, LINES);
        uriMatcher.addURI(LineContract.AUTHORITY, LineContract.LINE_PATH + "/#", LINES_WITH_ID);
        uriMatcher.addURI(PaintingContract.AUTHORITY, PaintingContract.PAINTING_PATH, PAINTING);
        uriMatcher.addURI(PaintingContract.AUTHORITY, PaintingContract.PAINTING_PATH + "/#", PAINTING_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        drawingSQLiteHelper = new DrawingSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = drawingSQLiteHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor cursor = null;
        String id;
        switch (match) {
            case POINTS_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    cursor = sqLiteDatabase.query(
                            PointContract.PointEntry.TABLE_NAME, projection, "_id = ?",
                            new String[]{id}, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case POINTS:
                try {
                    cursor = sqLiteDatabase.query(
                            PointContract.PointEntry.TABLE_NAME, projection, selection,
                            selectionArgs, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case LINES_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    cursor = sqLiteDatabase.query(
                            LineContract.LineEntry.TABLE_NAME, projection, "_id = ?",
                            new String[]{id}, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case LINES:
                try {
                    cursor = sqLiteDatabase.query(
                            LineContract.LineEntry.TABLE_NAME, projection, selection,
                            selectionArgs, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case PAINTING_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    cursor = sqLiteDatabase.query(
                            PaintingContract.PaintingEntry.TABLE_NAME, projection, "_id = ?",
                            new String[]{id}, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case PAINTING:
                try {
                    cursor = sqLiteDatabase.query(
                            PaintingContract.PaintingEntry.TABLE_NAME, projection, selection,
                            selectionArgs, null, null, sortOrder
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqLiteDatabase = drawingSQLiteHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        long id = 0;
        switch (match) {
            case POINTS:
                try {
                    id = sqLiteDatabase.insert(
                            PointContract.PointEntry.TABLE_NAME, null, values
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case LINES:
                try {
                    id = sqLiteDatabase.insert(
                            LineContract.LineEntry.TABLE_NAME, null, values
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case PAINTING:
                try {
                    id = sqLiteDatabase.insert(
                            PaintingContract.PaintingEntry.TABLE_NAME, null, values
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            uri = uri.buildUpon().appendPath("" + id).build();
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = drawingSQLiteHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        String id;
        int numberOfRows = 0;
        switch(match) {
            case POINTS_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.delete(
                            PointContract.PointEntry.TABLE_NAME, "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case LINES_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.delete(
                            LineContract.LineEntry.TABLE_NAME, "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case PAINTING_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.delete(
                            PaintingContract.PaintingEntry.TABLE_NAME, "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRows == 1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = drawingSQLiteHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        String id;
        int numberOfRows = 0;
        switch(match) {
            case POINTS_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.update(
                            PointContract.PointEntry.TABLE_NAME, values, "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case LINES_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.update(
                            LineContract.LineEntry.TABLE_NAME, values, "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            case PAINTING_WITH_ID:
                id = uri.getLastPathSegment();
                try {
                    numberOfRows = sqLiteDatabase.update(
                            PaintingContract.PaintingEntry.TABLE_NAME,values,  "_id = ?", new String[]{id}
                    );
                } catch (SQLiteException e) {
                    Log.e(getContext().getString(R.string.tag_error_sqlite), e.getStackTrace().toString());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRows == 1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRows;
    }
}

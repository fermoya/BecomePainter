package com.example.fmoyader.becomepainter.sqlite.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 26/5/17.
 */

public class PaintingContract {

    public static final String AUTHORITY = "com.example.fmoyader.becomepainter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PAINTING_PATH = PaintingEntry.TABLE_NAME;

    public static class PaintingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PAINTING_PATH).build();

        public static final String TABLE_NAME = "paintings";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AUTHOR = "author";
    }
}

package com.example.fmoyader.becomepainter.sqlite.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 26/5/17.
 */

public class LineContract {

    public static final String AUTHORITY = "com.example.fmoyader.becomepainter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String LINE_PATH = LineEntry.TABLE_NAME;

    public static class LineEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(LINE_PATH).build();

        public static final String TABLE_NAME = "lines";

        public static final String COLUMN_COLOR = "color_id";
        public static final String COLUMN_PAINTING_ID = "painting_id";
    }
}

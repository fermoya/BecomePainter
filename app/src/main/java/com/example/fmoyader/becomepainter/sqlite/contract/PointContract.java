package com.example.fmoyader.becomepainter.sqlite.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fmoyader on 26/5/17.
 */

public class PointContract {

    public static final String AUTHORITY = "com.example.fmoyader.becomepainter";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String POINT_PATH = PointEntry.TABLE_NAME;

    public static class PointEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(POINT_PATH).build();

        public static final String TABLE_NAME = "points";

        public static final String COLUMN_X = "coordinate_x";
        public static final String COLUMN_Y = "coordinate_y";
        public static final String COLUMN_LINE_ID = "line_id";
    }
}

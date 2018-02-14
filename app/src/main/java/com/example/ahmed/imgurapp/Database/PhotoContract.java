package com.example.ahmed.imgurapp.Database;


import android.net.Uri;
import android.provider.BaseColumns;

public class PhotoContract {
    static final String AUTHORITY = "com.example.ahmed.imgurapp";
    static final String PATH_PHOTOS = "Photos";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class PhotoEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTOS).build();

        static final String TABLE_NAME = "Photos";

        static final String COLUMN_ID = "id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_COVER = "cover";
        static final String COLUMN_IS_ALBUM = "is_album";
        static final String COLUMN_IMAGES_COUNT = "images_count";
        static final String COLUMN_IMAGES = "images";
        static final String COLUMN_TAGS = "tags";
        static final String COLUMN_HEIGHT = "height";
        static final String COLUMN_WIDTH = "width";
    }
}

package com.example.ahmed.imgurapp.Database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PhotoProvider extends ContentProvider {

    private static final int PHOTOS = 100;
    private static final int PHOTO_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private PhotoDbHelper photoDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PhotoContract.AUTHORITY, PhotoContract.PATH_PHOTOS, PHOTOS);
        uriMatcher.addURI(PhotoContract.AUTHORITY, PhotoContract.PATH_PHOTOS + "/*", PHOTO_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        photoDbHelper = new PhotoDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection
            , @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = photoDbHelper.getReadableDatabase();

        Cursor returnCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PHOTOS:
                returnCursor = db.query(PhotoContract.PhotoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PHOTO_WITH_ID:

                returnCursor = db.query(PhotoContract.PhotoEntry.TABLE_NAME,
                        projection,
                        PhotoContract.PhotoEntry.COLUMN_ID + "=?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (returnCursor != null && getContext() != null)
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = photoDbHelper.getWritableDatabase();

        Uri returnUri = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PHOTOS:
                long id = db.insert(PhotoContract.PhotoEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = photoDbHelper.getWritableDatabase();

        int tasksDeleted;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PHOTOS:
                tasksDeleted = db.delete(PhotoContract.PhotoEntry.TABLE_NAME,
                        null,
                        null);
                break;
            case PHOTO_WITH_ID:
                tasksDeleted = db.delete(PhotoContract.PhotoEntry.TABLE_NAME,
                        PhotoContract.PhotoEntry.COLUMN_ID + "=?",
                        new String[]{uri.getPathSegments().get(1)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (tasksDeleted != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String
            s, @Nullable String[] strings) {
        return 0;
    }
}

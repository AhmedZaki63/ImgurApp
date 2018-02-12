package com.example.ahmed.imgurapp.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.ahmed.imgurapp.Database.PhotoContract.PhotoEntry;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Models.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PhotoDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Photos.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                PhotoEntry.TABLE_NAME + " (" +
                PhotoEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                PhotoEntry.COLUMN_TITLE + " TEXT , " +
                PhotoEntry.COLUMN_DESCRIPTION + " TEXT , " +
                PhotoEntry.COLUMN_COVER + " TEXT , " +
                PhotoEntry.COLUMN_IS_ALBUM + " INTEGER , " +
                PhotoEntry.COLUMN_IMAGES_COUNT + " INTEGER , " +
                PhotoEntry.COLUMN_IMAGES + " TEXT , " +
                PhotoEntry.COLUMN_TAGS + " TEXT , " +
                PhotoEntry.COLUMN_HEIGHT + " INTEGER NOT NULL, " +
                PhotoEntry.COLUMN_WIDTH + " INTEGER NOT NULL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Photo> getAllFromDatabase() {
        Cursor cursor = context.getContentResolver()
                .query(PhotoEntry.CONTENT_URI, null, null, null, null);

        ArrayList<Photo> photos = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst())
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_DESCRIPTION));
                    String cover = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_COVER));
                    Integer is_album = cursor.getInt(cursor.getColumnIndex(PhotoEntry.COLUMN_IS_ALBUM));
                    Integer images_count = cursor.getInt(cursor.getColumnIndex(PhotoEntry.COLUMN_IS_ALBUM));

                    String imagesString = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_IMAGES));
                    Type imagesType = new TypeToken<ArrayList<Photo>>() {
                    }.getType();
                    ArrayList<Photo> images = new Gson().fromJson(imagesString, imagesType);

                    String tagsString = cursor.getString(cursor.getColumnIndex(PhotoEntry.COLUMN_TAGS));
                    Type tagsType = new TypeToken<ArrayList<Tag>>() {
                    }.getType();
                    ArrayList<Tag> tags = new Gson().fromJson(tagsString, tagsType);

                    Integer height = cursor.getInt(cursor.getColumnIndex(PhotoEntry.COLUMN_HEIGHT));
                    Integer width = cursor.getInt(cursor.getColumnIndex(PhotoEntry.COLUMN_WIDTH));

                    Photo photo = new Photo();
                    photo.setId(id);
                    photo.setTitle(title);
                    photo.setDescription(description);
                    photo.setCover(cover);
                    photo.setIs_album(is_album > 0);
                    photo.setImages_count(images_count);
                    photo.setImages(images);
                    photo.setTags(tags);
                    photo.setHeight(height);
                    photo.setWidth(width);

                    photos.add(photo);
                    cursor.moveToNext();
                }
            cursor.close();
        }

        return photos;
    }

    public boolean isSaved(Photo photo) {
        Uri uri = PhotoEntry.CONTENT_URI.buildUpon().appendPath(photo.getId()).build();
        return context.getContentResolver().query(uri, null, null, null, null)
                .getCount() > 0;
    }

    public void addToDatabase(Photo photo, ArrayList<Photo> photos) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoEntry.COLUMN_ID, photo.getId());
        contentValues.put(PhotoEntry.COLUMN_TITLE, photo.getTitle());
        contentValues.put(PhotoEntry.COLUMN_DESCRIPTION, photo.getDescription());
        contentValues.put(PhotoEntry.COLUMN_COVER, photo.getCover());
        contentValues.put(PhotoEntry.COLUMN_IS_ALBUM, photo.getIs_album());
        contentValues.put(PhotoEntry.COLUMN_IMAGES_COUNT, photo.getImages_count());

        String imagesString = new Gson().toJson(photos);
        contentValues.put(PhotoEntry.COLUMN_IMAGES, imagesString);

        String tagsString = new Gson().toJson(photo.getTags());
        contentValues.put(PhotoEntry.COLUMN_TAGS, tagsString);

        contentValues.put(PhotoEntry.COLUMN_HEIGHT, photo.getHeight());
        contentValues.put(PhotoEntry.COLUMN_WIDTH, photo.getWidth());

        context.getContentResolver().insert(PhotoEntry.CONTENT_URI, contentValues);
    }

    public void removeFromDatabase(Photo photo) {
        Uri uri = PhotoEntry.CONTENT_URI.buildUpon().appendPath(photo.getId()).build();
        context.getContentResolver().delete(uri, null, null);
    }

    public void removeAllFromDatabase() {
        context.getContentResolver().delete(PhotoEntry.CONTENT_URI, null, null);
    }
}

package com.example.ahmed.imgurapp.Widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ahmed.imgurapp.Database.PhotoContract;
import com.example.ahmed.imgurapp.Database.PhotoDbHelper;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.R;

import java.util.ArrayList;

class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Photo> photos;
    private Context context;
    private int appWidgetId;

    WidgetFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        PhotoDbHelper photoDbHelper = new PhotoDbHelper(context);
        Cursor cursor = context.getContentResolver()
                .query(PhotoContract.PhotoEntry.CONTENT_URI, null, null, null, null);
        photos = photoDbHelper.getAllFromDatabase(cursor);
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        Photo photo = photos.get(position);
        views.setTextViewText(R.id.widget_title, photo.getTitle());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
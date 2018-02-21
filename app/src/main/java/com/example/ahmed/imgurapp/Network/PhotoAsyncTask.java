package com.example.ahmed.imgurapp.Network;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.imgurapp.BuildConfig;
import com.example.ahmed.imgurapp.Models.AlbumResponse;
import com.example.ahmed.imgurapp.Models.PhotoResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoAsyncTask extends AsyncTask<String, Void, Object> {
    private OnLoadFinished onLoadFinished;

    public PhotoAsyncTask(OnLoadFinished onLoadFinished) {
        this.onLoadFinished = onLoadFinished;
    }

    @Override
    protected void onPreExecute() {
        onLoadFinished.onPreExecute();
    }

    @Override
    protected Object doInBackground(String... params) {
        if (params.length == 0)
            return null;

        HttpURLConnection urlConnection = null;
        Object response = null;

        try {
            final String BASE_URL = "https://api.imgur.com/3/gallery/";

            Uri.Builder builtUri = Uri.parse(BASE_URL).buildUpon();
            if (params.length == 1)
                builtUri.appendPath(params[0]);
            else
                builtUri.appendPath("hot")
                        .appendPath(params[0])
                        .appendPath(params[1]);

            builtUri.appendQueryParameter("client_id", BuildConfig.PHOTO_CLIENT_ID)
                    .build();


            URL url = new URL(builtUri.toString());
            Log.v("url", url.toString());

            // Open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            builder.append(line);

            Type responseType;
            if (params.length == 1) {
                responseType = new TypeToken<AlbumResponse>() {
                }.getType();
            } else {
                responseType = new TypeToken<PhotoResponse>() {
                }.getType();
            }

            response = new Gson().fromJson(builder.toString(), responseType);
        } catch (IOException e) {
            Log.e("AsyncTask", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(Object response) {
        onLoadFinished.onPostExecute(response);
    }
}

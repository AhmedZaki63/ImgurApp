package com.example.ahmed.imgurapp;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ahmed.imgurapp.Adapters.PhotoAdapter;
import com.example.ahmed.imgurapp.Database.PhotoContract;
import com.example.ahmed.imgurapp.Database.PhotoDbHelper;
import com.example.ahmed.imgurapp.Models.Photo;
import com.example.ahmed.imgurapp.Models.PhotoResponse;
import com.example.ahmed.imgurapp.Network.PhotoApi;
import com.example.ahmed.imgurapp.Network.PhotoClient;
import com.example.ahmed.imgurapp.Util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    ArrayList<Photo> photos;
    PhotoAdapter photoAdapter;
    EndlessRecyclerViewScrollListener viewScrollListener;
    PhotoApi photoApi;
    String sort;
    int page = 0;
    boolean preferenceChanged;

    @BindView(R.id.rv_of_photos)
    RecyclerView photoView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.main_progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        if (photos == null)
            photos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photos, getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        viewScrollListener =
                new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                    @Override
                    public void onLoadMore(int p, int totalItemsCount, RecyclerView view) {
                        if (!sort.equals("favourite")) {
                            page++;
                            fetchPhotosData();
                        }
                    }
                };

        photoView.setAdapter(photoAdapter);
        photoView.setLayoutManager(staggeredGridLayoutManager);
        photoView.addOnScrollListener(viewScrollListener);

        photoApi = PhotoClient.createApi(PhotoClient.buildRetrofit());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!sort.equals("favourite")) {
                    page = 0;
                    viewScrollListener.resetState();
                    fetchPhotosData();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort = prefs
                .getString("prefs_sort_list_key", getString(R.string.pref_default_sort));
        if (sort.equals("favourite")) {
            getActivity().getLoaderManager()
                    .initLoader(1, null, this);
        } else if (preferenceChanged || photos.isEmpty()) {
            page = 0;
            viewScrollListener.resetState();
            fetchPhotosData();
            preferenceChanged = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchPhotosData() {
        progressBar.setVisibility(View.VISIBLE);
        photoApi.getPhotosData("hot", sort, page
                , BuildConfig.PHOTO_CLIENT_ID).enqueue(new retrofit2.Callback<PhotoResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<PhotoResponse> call
                    , @NonNull retrofit2.Response<PhotoResponse> response) {
                Log.v("url", response.raw().request().url().toString());
                if (response.isSuccessful()) {
                    PhotoResponse photoResponse = response.body();
                    if (photoResponse != null) {
                        if (page == 0)
                            photos = photoResponse.getData();
                        else
                            photos.addAll(photoResponse.getData());
                        photoAdapter.setData(photos);
                    }
                    if (getView() != null)
                        Snackbar.make(getView(), "Data Updated!"
                                , Snackbar.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<PhotoResponse> call, @NonNull Throwable t) {
                if (getView() != null)
                    Snackbar.make(getView(), "Fail to Update Data!"
                            , Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), PhotoContract.PhotoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (sort.equals("favourite")) {
            PhotoDbHelper photoDbHelper = new PhotoDbHelper(getContext());
            photos = photoDbHelper.getAllFromDatabase(cursor);
            photoAdapter.setData(photos);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("prefs_sort_list_key"))
            preferenceChanged = true;
    }
}

package com.example.ahmed.imgurapp.Network;

import com.example.ahmed.imgurapp.Models.AlbumResponse;
import com.example.ahmed.imgurapp.Models.PhotoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface PhotoApi {

    @GET("{section}/{sort}/{page}")
    Call<PhotoResponse> getPhotosData(@Path("section") String section
            , @Path("sort") String sort
            , @Path("page") int page
            , @Query("client_id") String apiKey);

    @GET("{id}")
    Call<AlbumResponse> getAlbumData(@Path("id") String id
            , @Query("client_id") String apiKey);
}

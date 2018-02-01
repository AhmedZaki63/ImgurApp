package com.example.ahmed.imgurapp.Network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoClient {

    private static final String BASE_URL = "https://api.imgur.com/3/gallery/";

    public static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static PhotoApi createApi(Retrofit retrofit) {
        return retrofit.create(PhotoApi.class);
    }
}

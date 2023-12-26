package com.example.rentappartmentclient.retrofit.api;

import com.example.rentappartmentclient.model.database.Image;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageApi {

    @GET("/image/get-by-offer")
    Call<List<Image>> getImagesByOffer(@Query("offerId") int offerId);
}

package com.example.webService;

import com.example.entity.Event;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleAPI {
    public static final String BASE_URL="https://www.googleapis.com/oauth2/v4/";

    @POST("token")
    @Headers("Content-Type:application/json")
    Call<JsonObject> getToken(@Body JsonObject jsonObject);
    }

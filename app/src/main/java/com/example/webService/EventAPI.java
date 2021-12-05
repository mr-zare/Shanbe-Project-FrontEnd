package com.example.webService;

import android.content.SharedPreferences;

import com.example.entity.Event;
import com.example.entity.Task;
import com.example.entity.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EventAPI {
    public static final String BASE_URL="https://shanbe-back.herokuapp.com/";
    @Headers({"Accept: application/json",
            "Content-Type: application/json"})

    @POST("event-create/")
    Call<Event> event_create(@Header("Authorization") String user_token, @Body Event event);

    @POST("enter-event-token/")
    Call<Event> enter_event_token(@Header("Authorization") String event_token, @Body JsonObject body);

    @GET("event-get/")
    Call<List<Event>> event_get(@Header("Authorization") String user_token);

    @POST("event-search/")
    Call<List<Event>> event_search(@Header("Authorization") String user_token,@Body JsonObject jsonObject);
}

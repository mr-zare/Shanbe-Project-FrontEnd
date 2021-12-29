package com.example.webService;

import android.content.SharedPreferences;

import com.example.entity.Event;
import com.example.entity.Session;
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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EventAPI {
    public static final String BASE_URL="https://shanbe-back.herokuapp.com/";
    @Headers({"Accept: application/json",
            "Content-Type: application/json"})

    @POST("events/create/")
    Call<Event> event_create(@Header("Authorization") String user_token, @Body Event event);

    @POST("events/enter-event-token/")
    Call<Event> enter_event_token(@Header("Authorization") String event_token, @Body JsonObject body);

    @POST("events/session-join/")
    Call<Session> join_session(@Header("Authorization") String event_token, @Body JsonObject body);

    @GET("events/get/")
    Call<List<Event>> event_get(@Header("Authorization") String user_token);

    @POST("events/search/")
    Call<List<Event>> event_search(@Header("Authorization") String user_token,@Body JsonObject jsonObject);


    @POST("events/created-get/")
    Call<List<Event>> event_created_get(@Header("Authorization") String user_token);

    @PATCH("events/edit/")
    Call<Event> event_edit(@Header("Authorization") String user_token,@Body Event event);

    @HTTP(method = "DELETE", path = "events/session-delete/", hasBody = true)
    Call<JsonObject> session_delete(@Header("Authorization") String user_token, @Body JsonObject jsonObject);

    @POST("events/session-get/")
    Call<List<Session>> session_get(@Header("Authorization") String user_token);

    @POST("events/session-cancel/")
    Call<JsonObject> session_cancel(@Header("Authorization") String user_token,@Body JsonObject sessionToken);


    @POST("events/session-get-day/")
    Call<List<Session>> session_get_day(@Header("Authorization") String user_token,@Body JsonObject sessionToken);

    @POST("events/session-users/")
    Call<List<User>> session_users(@Header("Authorization") String user_token,@Body JsonObject sessionToken);

    @POST("calendar/login-create/")
    Call<JsonObject> send_tokens(@Header("Authorization") String user_toekn,@Body JsonObject tokens);

}

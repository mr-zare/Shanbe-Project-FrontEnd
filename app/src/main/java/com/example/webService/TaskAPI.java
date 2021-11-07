package com.example.webService;

import com.example.entity.Task;
import com.example.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TaskAPI {
    public static final String BASE_URL="https://shanbe-back.herokuapp.com/";
    @Headers({"Accept: application/json"})

    @POST("task-create/")
    Call<Task> createTask(@Header("Content-Type") String content_type, @Body Task task);

}

package com.levelupexp.android.droidtalk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DroidTalkAPI {
    String ENDPOINT = "http://wwc-chatroom-staging.herokuapp.com/";

    @GET("/messages")
    Call<List<Message>> getMessages();

    @POST("/messages")
    Call<Message> sendMessage(@Body Message message);

}

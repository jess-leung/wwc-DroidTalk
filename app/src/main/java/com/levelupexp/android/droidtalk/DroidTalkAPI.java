package com.levelupexp.android.droidtalk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DroidTalkAPI {
    String ENDPOINT = "http://wwc-chatroom-staging.herokuapp.com/";

    @GET("/messages")
    Call<List<Message>> getMessages();

}

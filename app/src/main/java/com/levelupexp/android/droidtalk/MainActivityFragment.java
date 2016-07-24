package com.levelupexp.android.droidtalk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivityFragment extends Fragment {

    private static final String BASE_URL = "http://wwc-chatroom-staging.herokuapp.com/";
    private static final String MESSAGE_RETRIEVAL_ERROR_MESSAGE = "Unable to retrieve messages";
    private static final String MESSAGE_SEND_ERROR_MESSAGE = "Unable to send message";
    @BindView(R.id.message_list_view)
    ListView messageListView;

    @BindView(R.id.message_button)
    Button messageSendButton;

    @BindView(R.id.message_input)
    EditText messageInputBox;

    private Unbinder unbinder;
    private List<String> list;
    private ArrayAdapter arrayAdapter;
    private DroidTalkAPI droidTalkAPI;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setUpListView();
        setUpMessages();

        return rootView;
    }

    private void setUpMessages() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        droidTalkAPI = retrofit.create(DroidTalkAPI.class);
        getMessages();
    }

    private void setUpListView() {
        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        messageListView.setAdapter(arrayAdapter);
    }

    private void getMessages() {
        Call<List<Message>> messages = droidTalkAPI.getMessages();
        messages.enqueue(new Callback<List<Message>>() {

            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messages = response.body();
                list.clear();
                for (Message message : messages) {
                    list.add(message.content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getContext(), MESSAGE_RETRIEVAL_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.message_button)
    public void submit() {
        String inputText = messageInputBox.getText().toString();
        Call<Message> message = droidTalkAPI.sendMessage(new Message(inputText));
        message.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                getMessages();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getContext(), MESSAGE_SEND_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            }
        });
        messageInputBox.setText("");
        arrayAdapter.notifyDataSetChanged();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

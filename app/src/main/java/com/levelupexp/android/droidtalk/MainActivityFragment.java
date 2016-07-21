package com.levelupexp.android.droidtalk;

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

        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        messageListView.setAdapter(arrayAdapter);

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://wwc-chatroom-staging.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        droidTalkAPI = retrofit.create(DroidTalkAPI.class);
        getMessages();

        return rootView;
    }

    private void getMessages() {
        Call<List<Message>> messages = droidTalkAPI.getMessages();
        messages.enqueue(new Callback<List<Message>>() {

            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messages = response.body();
                for (Message message : messages) {
                    list.add(message.content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to retrieve messages", Toast.LENGTH_LONG).show();
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
                list.clear();
                getMessages();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to send message", Toast.LENGTH_LONG).show();
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

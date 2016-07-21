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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        list = new ArrayList<>();
        list.add("This is the first message");
        list.add("This is the second message");
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        messageListView.setAdapter(arrayAdapter);
        return rootView;
    }

    @OnClick(R.id.message_button)
    public void submit() {
        String inputText = messageInputBox.getText().toString();
        list.add(inputText);
        messageInputBox.setText("");
        arrayAdapter.notifyDataSetChanged();

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

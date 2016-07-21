package com.levelupexp.android.droidtalk;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivityFragment extends Fragment {

    @BindView(R.id.message_list_view)
    ListView messageListView;

    private Unbinder unbinder;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        List<String> list = new ArrayList<>();
        list.add("This is the first message");
        list.add("This is the second message");
        ListAdapter listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
        messageListView.setAdapter(listAdapter);
        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

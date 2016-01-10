package com.bigfat.viewpagerinscrollviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueban on 23/8/15.
 */
public class TabFragment extends Fragment {
    private ListView listView;

    public static TabFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getArguments().getString("title");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.activity_list_item, android.R.id.text1, generateData(title, 50));
        listView.setAdapter(adapter);
    }

    private List<String> generateData(String title, int capacity) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            result.add(title + "-" + i);
        }
        return result;
    }
}
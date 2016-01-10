package com.bigfat.materialviewpagertest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import java.util.ArrayList;

/**
 * Created by yueban on 19/8/15.
 */
public class TabFragment extends Fragment {
    private RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewMaterialAdapter(new TabRecyclerViewAdapter(getActivity(), generateData(getArguments().getString("title"), 50))));
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }

    private ArrayList<String> generateData(String title, int size) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(title + "-" + i);
        }
        return data;
    }
}
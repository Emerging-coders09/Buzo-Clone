package com.devarshi.buzoclone;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devarshi.Adapter.ListAdapterSaved;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class SavedFragment extends Fragment {

    String SAVED_FILES_LOCATION = "/Buzo-VideoStatusMakerOne/StatusDownloader-Buzo-VideoStatusMaker";
    RecyclerView mRecyclerViewMediaList;
    SwipeRefreshLayout swipeRefreshLayoutSaved;

    public SavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_saver, container, false);

        mRecyclerViewMediaList = view.findViewById(R.id.recyclerViewMediaSaved);
        swipeRefreshLayoutSaved = view.findViewById(R.id.swipeRefreshSaved);

        swipeRefreshLayoutSaved.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                mRecyclerViewMediaList.setLayoutManager(staggeredGridLayoutManager);
                ListAdapterSaved listAdapterSaved = new ListAdapterSaved(getActivity(),SavedFragment.this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + SAVED_FILES_LOCATION)));
                mRecyclerViewMediaList.setAdapter(listAdapterSaved);
                swipeRefreshLayoutSaved.setRefreshing(false);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewMediaList.setLayoutManager(staggeredGridLayoutManager);
        ListAdapterSaved listAdapterSaved = new ListAdapterSaved(getActivity(),this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + SAVED_FILES_LOCATION)));
        mRecyclerViewMediaList.setAdapter(listAdapterSaved);

        return view;
    }

    @NotNull
    public ArrayList<File> getListFiles(@NotNull File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif") || file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file)) {
                        inFiles.add(file);
                    }
                }
                Log.i("FileExistence", file.getName());
            }
        }
        return inFiles;
    }
}
package com.devarshi.buzoclone;

import android.content.Context;
import android.os.Build;
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

import com.devarshi.Adapter.ListAdapterStatus;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class StatusViewerFragment extends Fragment {

    String WHATSAPP_STATUSES_LOCATION = Build.VERSION.SDK_INT <=28 ? "/WhatsApp/Media/.Statuses" : (Build.VERSION.SDK_INT == 30 ? "Android/media/com.whatsapp/WhatsApp/Media/.Statuses" : (Build.VERSION.SDK_INT == 29)? "/WhatsApp/Media/.Statuses" : "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");
//    String WHATSAPP_STATUSES_LOCATION = Build.VERSION.SDK_INT<=30  ? "WhatsApp/Media/.Statuses":"Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
    RecyclerView mRecyclerViewMediaList;
    SwipeRefreshLayout swipeRefreshStatus;
    Context mContext;

//    private FirebaseAnalytics mFirebaseAnalytics;

//    String WHATSAPP_STATUSES_LOCATION = "Android/media/com.whatsapp/WhatsApp/Media/.Statuses";

    public StatusViewerFragment() {
        // Required empty public constructor

    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_viewer, container, false);
        mRecyclerViewMediaList = view.findViewById(R.id.recyclerViewMediaStatus);

        swipeRefreshStatus = view.findViewById(R.id.swipeRefreshStatus);

        swipeRefreshStatus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                mRecyclerViewMediaList.setLayoutManager(staggeredGridLayoutManager);
                ListAdapterStatus recyclerViewMediaAdapter = new ListAdapterStatus(getActivity(), StatusViewerFragment.this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION)));
                mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);
                swipeRefreshStatus.setRefreshing(false);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewMediaList.setLayoutManager(staggeredGridLayoutManager);
        ListAdapterStatus recyclerViewMediaAdapter = new ListAdapterStatus(getActivity(),this.getListFiles(new File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUSES_LOCATION)));
        mRecyclerViewMediaList.setAdapter(recyclerViewMediaAdapter);

        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        StatusViewerFragment fragment = new StatusViewerFragment();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, fragment.getClass().getSimpleName());
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, fragment.getClass().getSimpleName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);*/

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
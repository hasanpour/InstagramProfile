package com.example.instagramprofile.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.example.instagramprofile.R;
import com.example.instagramprofile.adapters.GridAdapter;
import com.example.instagramprofile.asyncTasks.GetMediaAsyncTask;
import com.example.instagramprofile.interfaces.ClickListener;
import com.example.instagramprofile.interfaces.MediaAsyncResponseInterface;
import com.example.instagramprofile.model.Media;
import com.example.instagramprofile.util.RecyclerTouchListener;


public class GridFragment extends Fragment implements MediaAsyncResponseInterface {

    //region Private Members
    private GetMediaAsyncTask getMediaAsyncTask = new GetMediaAsyncTask();
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    private List<Media> mediaList = new ArrayList<Media>();
    //endregion

    public GridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getMediaAsyncTask.delegate = this;
        getMediaAsyncTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        gridAdapter = new GridAdapter(getActivity(), mediaList);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewImages);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);

        if (recyclerView != null) {

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(gridAdapter);
            // Show image and video on Instagram on click
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Media media = mediaList.get(position);
                    String videoURL = media.getMediaURL();
                    if (videoURL != null){
                        Uri uri = Uri.parse(media.getMediaURL());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.instagram.android");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(media.getMediaURL())));
                        }
                    }
                }

                @Override
                public void onLongClick(View view, int position) {
                }
            }));
        }

        return rootView;
    }

    /**
     * Refresh UI with images
     *
     * @param medias List of images
     */
    @Override
    public void getMediaFinish(List<Media> medias) {
        for (Media media :
                medias) {
            mediaList.add(media);
        }
        gridAdapter.notifyDataSetChanged();
    }
}

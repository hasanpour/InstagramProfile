package ir.cafebazaar.cafein.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.cafebazaar.cafein.R;
import ir.cafebazaar.cafein.adapters.GridAdapter;
import ir.cafebazaar.cafein.asyncTasks.GetMediaAsyncTask;
import ir.cafebazaar.cafein.interfaces.MediaAsyncResponseInterface;
import ir.cafebazaar.cafein.model.Media;


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
    public void onResume() {
        super.onResume();
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

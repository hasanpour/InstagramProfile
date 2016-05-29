package ir.cafebazaar.cafein.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ir.cafebazaar.cafein.MainActivity;
import ir.cafebazaar.cafein.R;
import ir.cafebazaar.cafein.adapters.ListAdapter;
import ir.cafebazaar.cafein.model.Media;
import ir.cafebazaar.cafein.util.DividerItemDecoration;
import ir.cafebazaar.cafein.util.EndlessRecyclerOnScrollListener;
import ir.cafebazaar.cafein.util.Util;


public class ListFragment extends Fragment {

    //region Private Members
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Media> mediaList = new ArrayList<Media>();
    private static String maxID = null; // Last image id in list
    private boolean loaded = false; // true when get all images
    //endregion

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        maxID = null;
        new getMedia().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        listAdapter = new ListAdapter(getActivity(), mediaList);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewImages);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        if (recyclerView != null) {

            recyclerView.setLayoutManager(linearLayoutManager);
            /* Get images when scroll */
            recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    if (!loaded)
                        new getMedia().execute();
                }
            });
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(listAdapter);
        }

        return rootView;
    }

    /**
     * Get user's media
     */
    public class getMedia extends AsyncTask<Void, Void, List<Media>> {

        //region Private Members
        private final String APIURL = "https://api.instagram.com/v1/users/self/media/recent/";
        private final String ACCESSTOKEN = MainActivity.sharedPreferences.getString("prf_access_token", null);
        private final String COUNT = "5"; // Number of images that we get in each request
        private Util util = new Util();
        //endregion

        @Override
        protected List<Media> doInBackground(Void... voids) {

            String tokenURLString = APIURL + "?access_token=" + ACCESSTOKEN + "&count=" + COUNT;

            if (ListFragment.maxID != null)
                tokenURLString += "&max_id=" + ListFragment.maxID;

            List<Media> images = new ArrayList<>();

            try {

                /* Setup connection */
                URL url = new URL(tokenURLString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                /* Get Response */
                String jsonString = util.getString(httpsURLConnection);
                JSONObject jsonObject = (JSONObject) new JSONTokener(jsonString).nextValue();
                try { // Handle end of the images!
                    ListFragment.maxID = jsonObject.getJSONObject("pagination").getString("next_max_id");
                } catch (JSONException e) {
                    loaded = true;
                    e.printStackTrace();
                }
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                // Iterate through the elements of the array i
                for (int i = 0; i < jsonArray.length(); i++) {
                    String caption = null;
                    String time = jsonArray.getJSONObject(i).getString("created_time");
                    String imageURL = jsonArray.getJSONObject(i).getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                    try {
                        caption = jsonArray.getJSONObject(i).getJSONObject("caption").getString("text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Add data to list
                    Media image = new Media();
                    image.setTime(time);
                    image.setImageURL(imageURL);
                    image.setCaption(caption);
                    images.add(image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return images;
        }

        @Override
        protected void onPostExecute(List<Media> images) {
            if (images.size() != 0)
                for (Media media :
                        images) {
                    mediaList.add(media);
                }
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerView.clearOnScrollListeners();
    }
}

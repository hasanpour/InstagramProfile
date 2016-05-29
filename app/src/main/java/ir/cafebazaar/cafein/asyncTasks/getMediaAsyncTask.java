package ir.cafebazaar.cafein.asyncTasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ir.cafebazaar.cafein.MainActivity;
import ir.cafebazaar.cafein.interfaces.MediaAsyncResponseInterface;
import ir.cafebazaar.cafein.model.Media;
import ir.cafebazaar.cafein.util.Util;

/**
 * Get user's media
 */
public class GetMediaAsyncTask extends AsyncTask<Void, Void, List<Media>> {

    private final String APIURL = "https://api.instagram.com/v1/users/self/media/recent/";
    private final String ACCESSTOKEN = MainActivity.sharedPreferences.getString("prf_access_token", null);
    // Using interface to refresh UI on result.
    public MediaAsyncResponseInterface delegate = null;
    //region Private Members
    private Util util = new Util();
    //endregion

    @Override
    protected List<Media> doInBackground(Void... voids) {

        String tokenURLString = APIURL + "?access_token=" + ACCESSTOKEN;

        List<Media> images = new ArrayList<>();

        try {

            /* Setup connection */
            URL url = new URL(tokenURLString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            /* Get Response */
            String jsonString = util.getString(httpsURLConnection);
            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonString).nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            // Iterate through the elements of the array i
            for(int i = 0; i < jsonArray.length(); i++) {
                String caption=null;
                String time = jsonArray.getJSONObject(i).getString("created_time");
                String imageURL = jsonArray.getJSONObject(i).getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                try{ // Handle null caption!
                    caption = jsonArray.getJSONObject(i).getJSONObject("caption").getString("text");
                } catch (JSONException e){
                    e.printStackTrace();
                }
                // Add data to list
                Media image = new Media();
                image.setTime(time);
                image.setMediaURL(imageURL);
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
            delegate.getMediaFinish(images);
    }
}
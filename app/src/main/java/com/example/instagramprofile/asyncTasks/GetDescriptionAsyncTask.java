package com.example.instagramprofile.asyncTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.example.instagramprofile.MainActivity;
import com.example.instagramprofile.interfaces.DescriptionAsyncResponseInterface;
import com.example.instagramprofile.util.Util;

/**
 * Get profile description
 */
public class GetDescriptionAsyncTask extends AsyncTask<Void, Void, String> {

    //region Private Members
    private final String APIURL = "https://api.instagram.com/v1/users/self/";
    private final String ACCESSTOKEN = MainActivity.sharedPreferences.getString("prf_access_token", null);
    private Util util = new Util();
    //endregion

    // Using interface to refresh UI on result.
    public DescriptionAsyncResponseInterface delegate = null;

    @Override
    protected String doInBackground(Void... voids) {

        String description = null;
        String tokenURLString = APIURL + "?access_token=" + ACCESSTOKEN;

        try {

            /* Setup connection */
            URL url = new URL(tokenURLString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            /* Get Response */
            String jsonString = util.getString(httpsURLConnection);
            JSONObject jsonObject = (JSONObject) new JSONTokener(jsonString).nextValue();
            description = jsonObject.getJSONObject("data").getString("bio");

            /* Save Info to SharedPreferences */
            SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
            editor.putString("prf_description", description);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return description;
    }

    @Override
    protected void onPostExecute(String description) {
        if (description != null)
            delegate.getDescriptionFinish(description);
    }
}
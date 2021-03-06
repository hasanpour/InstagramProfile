package com.example.instagramprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.instagramprofile.util.Util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LogInActivity extends AppCompatActivity {

    //region Private Members
    private final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    private final String CALLBACKURL = "instagramprofile://connect";
    private Util util;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        util = new Util();

        String authURLString = AUTHURL +
                "?client_id=" + getString(R.string.client_id) +
                "&redirect_uri=" + CALLBACKURL +
                "&response_type=code";

        /* Show Login Page */
        WebView webView = (WebView) findViewById(R.id.webView);
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new AuthWebView());
            webView.loadUrl(authURLString);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    /**
     * Get a auth code
     */
    public class AuthWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(CALLBACKURL)) {

                System.out.println(url);
                String parts[] = url.split("=");
                String CODE = parts[1];

                new getToken().execute(CODE);

                return true;
            }
            return false;
        }
    }

    /**
     * Get Access Token
     */
    public class getToken extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            String tokenURLString = TOKENURL +
                    "?client_id=" + getString(R.string.client_id) +
                    "&client_secret=" + getString(R.string.client_secret) +
                    "&redirect_uri=" + CALLBACKURL +
                    "&grant_type=authorization_code";

            try {

                /* Setup connection */
                URL url = new URL(tokenURLString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                /* POST */
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + getString(R.string.client_id) +
                        "&client_secret=" + getString(R.string.client_secret) +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + CALLBACKURL +
                        "&code=" + strings[0]);
                outputStreamWriter.flush();

                /* Get Response */
                String jsonString = util.getString(httpsURLConnection);
                JSONObject jsonObject = (JSONObject) new JSONTokener(jsonString).nextValue();
                String accessToken = jsonObject.getString("access_token");
                String id = jsonObject.getJSONObject("user").getString("id");
                String username = jsonObject.getJSONObject("user").getString("username");
                String name = jsonObject.getJSONObject("user").getString("full_name");
                String profilePicture = jsonObject.getJSONObject("user").getString("profile_picture");

                /* Save Info to SharedPreferences */
                SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                editor.putString("prf_access_token", accessToken);
                editor.putString("id", id);
                editor.putString("prf_username", username);
                editor.putString("prf_name", name);
                editor.putString("prf_profile_picture", profilePicture);
                editor.apply();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /* Start Profile Activity */
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }
    }
}

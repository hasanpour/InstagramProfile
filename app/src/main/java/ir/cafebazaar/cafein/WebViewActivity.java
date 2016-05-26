package ir.cafebazaar.cafein;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WebViewActivity extends AppCompatActivity {

    //region Private Members
    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    private static final String CALLBACKURL = "cafein://connect";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

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

    /**
     * Get a auth code
     */
    public class AuthWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(CALLBACKURL)) {

                /* TODO: Handle errors. */

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

                URL url = new URL(tokenURLString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + getString(R.string.client_id) +
                        "&client_secret=" + getString(R.string.client_secret) +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + CALLBACKURL +
                        "&code=" + strings[0]);
                outputStreamWriter.flush();

                /* Get Response */
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader;
                String line;

                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();

                JSONObject jsonObject = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
                String accessToken = jsonObject.getString("access_token");
                String id = jsonObject.getJSONObject("user").getString("id");
                String username = jsonObject.getJSONObject("user").getString("username");
                String name = jsonObject.getJSONObject("user").getString("full_name");
                String profilePicture = jsonObject.getJSONObject("user").getString("profile_picture");

                stringBuilder.setLength(0);

                /* Save Info to SharedPreferences */
                savePrf(accessToken, id, username, name, profilePicture);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish(); // Return to MainActivity
        }
    }

    /**
     * Save User Info
     *
     * @param accessToken Access Token
     * @param id User ID
     * @param username Username
     * @param name Full Name
     * @param profilePicture Profile Picture URL
     */
    public void savePrf(String accessToken, String id, String username, String name, String profilePicture) {
        SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
        editor.putString("prf_access_token", accessToken);
        editor.putString("id", id);
        editor.putString("prf_username", username);
        editor.putString("prf_name", name);
        editor.putString("prf_profile_picture", profilePicture);
        editor.apply();
    }
}

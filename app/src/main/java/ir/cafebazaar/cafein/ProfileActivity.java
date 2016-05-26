package ir.cafebazaar.cafein;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageViewProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        imageViewProfilePicture = (ImageView) findViewById(R.id.imageViewProfilePicture);

        String name = MainActivity.sharedPreferences.getString("prf_name", null);
        String description = MainActivity.sharedPreferences.getString("prf_description", null);
        String profilePictureURL = MainActivity.sharedPreferences.getString("prf_profile_picture", null);


        if (textViewName != null) {
            textViewName.setText(name);
        }
        if (textViewDescription != null) {
            textViewDescription.setText(description);
        }

        new ImageLoader().execute(profilePictureURL);
    }

    public class ImageLoader extends AsyncTask<String, Void, RoundedBitmapDrawable> {

        @Override
        protected RoundedBitmapDrawable doInBackground(String... strings) {

            RoundedBitmapDrawable roundedBitmapDrawable = null;

            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), inputStream);
                roundedBitmapDrawable.setCircular(true);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return roundedBitmapDrawable;
        }

        @Override
        protected void onPostExecute(RoundedBitmapDrawable roundedBitmapDrawable) {
            super.onPostExecute(roundedBitmapDrawable);

            if (roundedBitmapDrawable.getBitmap() != null) {
                imageViewProfilePicture.setImageDrawable(roundedBitmapDrawable);
            }
        }
    }
}

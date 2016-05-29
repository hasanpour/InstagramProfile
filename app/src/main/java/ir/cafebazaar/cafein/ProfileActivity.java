package ir.cafebazaar.cafein;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ir.cafebazaar.cafein.adapters.ViewPagerAdapter;
import ir.cafebazaar.cafein.asyncTasks.GetDescriptionAsyncTask;
import ir.cafebazaar.cafein.fragments.GridFragment;
import ir.cafebazaar.cafein.fragments.ListFragment;
import ir.cafebazaar.cafein.interfaces.DescriptionAsyncResponseInterface;

public class ProfileActivity extends AppCompatActivity implements DescriptionAsyncResponseInterface {

    //region Private Members
    private TextView textViewDescription;
    private ImageView imageViewProfilePicture;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //endregion

    private GetDescriptionAsyncTask getDescriptionAsyncTask = new GetDescriptionAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* Get user's description */
        getDescriptionAsyncTask.delegate = this;
        getDescriptionAsyncTask.execute();

        /* Setup View Pager for tabs */
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Inflate Tab
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        setupTabIcons();

        //region inflate Profile Info
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        imageViewProfilePicture = (ImageView) findViewById(R.id.imageViewProfilePicture);

        /* Get info from shared preferences */
        String name = MainActivity.sharedPreferences.getString("prf_name", null);
        String description = MainActivity.sharedPreferences.getString("prf_description", null);
        String profilePictureURL = MainActivity.sharedPreferences.getString("prf_profile_picture", null);

        /* Show info */
        if (textViewName != null) {
            textViewName.setText(name);
        }
        if (textViewDescription != null) {
            textViewDescription.setText(description);
        }
        // Loading Profile Picture
        Picasso.with(this)
                .load(profilePictureURL)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Transform profile picture to circle!
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        roundedBitmapDrawable.setCircular(true);
                        imageViewProfilePicture.setImageDrawable(roundedBitmapDrawable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
        //endregion
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFragment(), "List");
        adapter.addFragment(new GridFragment(), "Grid");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_grid);
    }

    /**
     * Inflate Description
     *
     * @param description user's description
     */
    @Override
    public void getDescriptionFinish(String description) {
        textViewDescription.setText(description);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

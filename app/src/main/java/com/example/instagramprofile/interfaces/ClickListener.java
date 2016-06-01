package com.example.instagramprofile.interfaces;

import android.view.View;

/* http://www.androidhive.info/2016/01/android-working-with-recycler-view/ */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}

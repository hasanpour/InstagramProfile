package com.example.instagramprofile.interfaces;

import java.util.List;

import com.example.instagramprofile.model.Media;

/**
 * an interface for exchange list of media.
 */
public interface MediaAsyncResponseInterface {
    void getMediaFinish(List<Media> images);
}

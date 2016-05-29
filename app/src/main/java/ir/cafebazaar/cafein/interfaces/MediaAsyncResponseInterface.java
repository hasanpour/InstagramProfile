package ir.cafebazaar.cafein.interfaces;

import java.util.List;

import ir.cafebazaar.cafein.model.Media;

/**
 * an interface for exchange list of media.
 */
public interface MediaAsyncResponseInterface {
    void getMediaFinish(List<Media> images);
}

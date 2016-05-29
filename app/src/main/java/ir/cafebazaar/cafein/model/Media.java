package ir.cafebazaar.cafein.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Media {

    private String time;
    private String imageURL;
    private String mediaURL;
    private String caption;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Change time stamp to real date
     *
     * @return Date in "MM/dd/yyy" format
     */
    public String getDate() {
        Date date = new Date(Long.parseLong(getTime()) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}

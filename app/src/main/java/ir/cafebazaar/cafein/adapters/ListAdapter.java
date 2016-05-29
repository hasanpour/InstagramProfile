package ir.cafebazaar.cafein.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import ir.cafebazaar.cafein.R;
import ir.cafebazaar.cafein.model.Media;

/**
 * An adapter to inflate List View
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    //region Private Members
    private List<Media> mediaList;
    private Context context;
    //endregion

    public ListAdapter(Context context, List<Media> mediaList) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_images, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Media image = mediaList.get(position);

        /* Resize images to fit screen */
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.imageViewImage.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        holder.textViewDate.setText(image.getDate());
        Picasso.with(context).load(image.getImageURL()).transform(transformation).into(holder.imageViewImage); // Loading image using Picasso library
        holder.textViewCaption.setText(image.getCaption());
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public ImageView imageViewImage;
        public TextView textViewCaption;

        public MyViewHolder(View view) {
            super(view);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            imageViewImage = (ImageView) view.findViewById(R.id.imageViewImage);
            textViewCaption = (TextView) view.findViewById(R.id.textViewCaption);
        }
    }
}

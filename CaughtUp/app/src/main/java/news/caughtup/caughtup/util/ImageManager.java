package news.caughtup.caughtup.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import news.caughtup.caughtup.R;

/**
 * The ImageManager takes care of loading an image url asynchronously and setting the image of an imageView.
 */
public class ImageManager {
    private ImageView imageView;
    private Activity activity;

    /**
     * Constructor.
     * @param imageView
     * @param activity
     * @param url
     */
    public ImageManager(ImageView imageView, Activity activity, String url) {
        this.imageView = imageView;
        this.activity = activity;
        new LoadImage().execute(url);
    }

    /**
     * LoadImage extends AsyncTask to asynchronously load a remote image.
     */
    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        /**
         * Fetch the image data.
         * @param args
         * @return
         */
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * Handle the image.
         * @param image
         */
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                // Image loaded! Set the image for imageView.
                imageView.setImageBitmap(image);
            }else{
                // Image couldn't load... Displaying placeholder.
                imageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.profile_pic_1, null));
            }
        }
    }
}

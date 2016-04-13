package news.caughtup.caughtup.ui.prime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.ws.remote.FacebookManager;
import news.caughtup.caughtup.ws.remote.ISocialMediaManager;

public class NewsFeedAdapter extends ArrayAdapter<Article> {

    private Activity activity;
    public NewsFeedAdapter(ArrayList<Article> dataArray, Activity activity) {
        super(activity, android.R.layout.simple_list_item_1, dataArray);
        this.activity = activity;
    }
    private Article article;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if we weren't given a view, inflate one
        if (convertView == null) {
            convertView = this.activity.getLayoutInflater().inflate(R.layout.article_tile_view, null);
        }

        article = getItem(position);

        TextView titleTextView = (TextView)convertView.findViewById(R.id.name_article_tile_view);
        titleTextView.setText(article.getTitle());

        TextView descriptionTextView = (TextView)convertView.findViewById(R.id.description_article_tile_view);
        descriptionTextView.setText(String.valueOf(article.getSummary()));

        ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.thumbnail_article_tile_view);
        thumbnailImageView.setImageDrawable(activity.getResources().getDrawable(article.getThumbnailID(), null));

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, article.getArticleURI());
                activity.startActivity(launchBrowser);
                return false;
            }
        });

        ImageButton shareButton = (ImageButton) convertView.findViewById(R.id.share_article_tile_view);
        if(shareButton != null) {
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchShareDialog(article.getTitle());
                }
            });
        }
        return convertView;
    }

    private void launchShareDialog(final String articleName) {
        final String[] items = { "Share with Followers", "Share on Facebook", "Share on Twitter", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Share Article!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (items[item]) {
                    case "Share with Followers":
                        Toast.makeText(activity,
                                String.format("\"%s\" is now shared with your followers", articleName),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Share on Facebook":
                        String message = String.format("\"%s\" is now shared on Facebook", articleName);
                        ISocialMediaManager fbAccessManager = new FacebookManager();
                        fbAccessManager.authenticate();
                        fbAccessManager.share(message, article);
                        Toast.makeText(activity,
                                message,
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Share on Twitter":
                        Toast.makeText(activity,
                                String.format("\"%s\" is now shared on Twitter", articleName),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case "Cancel":
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }
}

package news.caughtup.caughtup.ui.prime;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.model.Article;

public class NewsFeedAdapter extends ArrayAdapter<Article> {
    Activity activity;
    public NewsFeedAdapter(ArrayList<Article> dataArray, Activity activity) {
        super(activity, android.R.layout.simple_list_item_1, dataArray);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if we weren't given a view, inflate one
        if (null == convertView) {
            convertView = this.activity.getLayoutInflater()
                    .inflate(R.layout.article_tile_view, null);
        }

        final Article article = getItem(position);

        TextView titleTextView =
                (TextView)convertView.findViewById(R.id.name_article_tile_view);
        titleTextView.setText(article.getTitle());

        TextView descriptionTextView =
                (TextView)convertView.findViewById(R.id.description_article_tile_view);
        descriptionTextView.setText(String.valueOf(article.getSummary()));

        ImageView thumbnailImageView =
                (ImageView) convertView.findViewById(R.id.thumbnail_article_tile_view);
        thumbnailImageView.setImageDrawable(activity.getResources().getDrawable(article.getThumbnailID(), null));

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, article.getArticleURI());
                activity.startActivity(launchBrowser);
                return false;
            }
        });
        return convertView;
    }
}

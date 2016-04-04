package news.caughtup.caughtup.ui.prime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import news.caughtup.caughtup.R;

public class ArticleTileView extends RelativeLayout {

    private ImageView thumbnail;
    private TextView name;
    private TextView description;

    public ArticleTileView(Context context) {
        super(context);
        init();
    }

    public ArticleTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleTileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.article_tile_view, this);
        this.thumbnail = (ImageView)findViewById(R.id.thumbnail_article_tile_view);
        this.name = (TextView)findViewById(R.id.name_article_tile_view);
        this.description = (TextView)findViewById(R.id.description_article_tile_view);
    }

    public void setThumbnailImage(int drawable_id) {
        thumbnail.setImageDrawable(getResources().getDrawable(drawable_id, null));
    }

    public void setNameText(String nameText) {
        name.setText(nameText);
    }

    public void setDescriptionText(String descriptionText) {
        description.setText(descriptionText);
    }
}

package news.caughtup.caughtup.ui.prime;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import news.caughtup.caughtup.R;

public class NewsSourceTileView extends RelativeLayout {

    private ImageView thumbnail;
    private TextView name;
    private TextView description;

    public NewsSourceTileView(Context context) {
        super(context);
        init();
    }

    public NewsSourceTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewsSourceTileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.news_source_tile_view, this);
        this.thumbnail = (ImageView)findViewById(R.id.thumbnail_news_source_tile_view);
        this.name = (TextView)findViewById(R.id.name_news_source_tile_view);
        this.description = (TextView)findViewById(R.id.description_news_source_tile_view);
    }

    public void setThumbnailImage(int drawable_id) {
        thumbnail.setImageDrawable(getResources().getDrawable(drawable_id, null));
    }

    public void setNameText(String nameText) {
        String visibleText = (nameText.length() > 40) ? nameText.substring(0, 37) + "..." : nameText;
        name.setText(visibleText);
    }

    public void setDescriptionText(String descriptionText) {
        String visibleText = (descriptionText.length() > 80) ? descriptionText.substring(0, 77) + "..." : descriptionText;
        description.setText(visibleText);
    }

}

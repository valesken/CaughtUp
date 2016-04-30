package news.caughtup.caughtup.ui.prime;

import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;

public class NewsFeedFragment extends ListFragment {
    private ArrayList<ICaughtUpItem> dataArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* For demonstration only! */
        populateDataArray();

        setListAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));
    }

    private void populateDataArray() {
        dataArray.add(new Article("Article Title 1", "11/2",
                R.mipmap.techcrunch_icon, "Summary of the news for article 1.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 2", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 2.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 3", "11/2", R.mipmap.techcrunch_icon, "Summary of the news for article 3.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 4", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 4.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 1", "11/2",
                R.mipmap.techcrunch_icon, "Summary of the news for article 1.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 2", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 2.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 3", "11/2", R.mipmap.techcrunch_icon, "Summary of the news for article 3.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 4", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 4.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 1", "11/2",
                R.mipmap.techcrunch_icon, "Summary of the news for article 1.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 2", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 2.",
                Uri.parse("http://www.google.com")));
        dataArray.add(new Article("Article Title 3", "11/2", R.mipmap.techcrunch_icon, "Summary of the news for article 3.",
                Uri.parse("http://www.bbc.com/")));
        dataArray.add(new Article("Article Title 4", "11/2", R.mipmap.bbc_icon, "Summary of the news for article 4.",
                Uri.parse("http://www.bbc.com/")));
    }
}

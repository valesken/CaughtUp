package news.caughtup.caughtup.ui.prime;

import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;
import news.caughtup.caughtup.entities.NewsSource;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.util.StringRetriever;

public class SearchFragment extends ListFragment {

    List<ICaughtUpItem> dataArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringRetriever retriever = StringRetriever.getInstance();
        ((HomeActivity) getActivity()).setToolbarTitle(retriever.getStringById(R.string.search_title));

        /* For demonstration only! */
        addTestItems();

        setListAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));
    }

    private void addTestItems() {
        addAllUsers();
        addTestArticles();
        addTestNewsSources();
    }

    private void addAllUsers() {
        for(User user : Users.getInstance()) {
            dataArray.add(user);
        }
    }

    private void addTestArticles() {
        // Techcrunch article
        Uri techcrunchUri = Uri.parse(getResources().getString(R.string.test_techcrunch_url));
        String techcrunchDate = getResources().getString(R.string.test_techcrunch_date);
        String techcrunchTitle = getResources().getString(R.string.test_techcrunch_title);
        String techcrunchDescription = getResources().getString(R.string.test_techcrunch_description);
        Article techcrunchArticle = new Article(techcrunchTitle, techcrunchDate, R.mipmap.techcrunch_icon,
                techcrunchDescription, techcrunchUri);
        // BBC article
        Uri bbcUri = Uri.parse(getResources().getString(R.string.test_bbc_url));
        String bbcDate = getResources().getString(R.string.test_bbc_date);
        String bbcTitle = getResources().getString(R.string.test_bbc_title);
        String bbcDescription = getResources().getString(R.string.test_bbc_description);
        Article bbcArticle = new Article(bbcTitle, bbcDate, R.mipmap.bbc_icon, bbcDescription, bbcUri);
        // Load Articles
        dataArray.add(techcrunchArticle);
        dataArray.add(bbcArticle);
    }

    private void addTestNewsSources() {
        // Techcrunch
        String techcrunchName = "Techcrunch";
        String techcrunchDescription = "TechCrunch is an online publisher of technology industry news.";
        String techcrunchUri = "http://techcrunch.com/";
        NewsSource techcrunch = new NewsSource(techcrunchName, techcrunchUri, R.mipmap.techcrunch_icon, techcrunchDescription);
        // BBC
        String bbcName = "BBC";
        String bbcUri = "http://www.bbc.com/";
        String bbcDescription = "The British Broadcasting Corporation (BBC) is the public service broadcaster of the United Kingdom, headquartered at Broadcasting House in London.";
        NewsSource bbc = new NewsSource(bbcName, bbcUri, R.mipmap.bbc_icon, bbcDescription);
        // Load news sources
        dataArray.add(techcrunch);
        dataArray.add(bbc);
    }
}

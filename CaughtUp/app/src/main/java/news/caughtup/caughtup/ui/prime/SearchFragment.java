package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;
import news.caughtup.caughtup.entities.NewsSource;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.util.StringRetriever;

public class SearchFragment extends Fragment {

    private View rootView;
    private StringRetriever retriever;
    private List<ICaughtUpItem> dataArray = new ArrayList<>();
    private String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle args = getArguments();
        query = args.getString("query");
        retriever = StringRetriever.getInstance();

        /* For demonstration only! */
        addTestItems();

        // Set up Results String
        setResultsString();

        // Set up Results
        ListView searchList = (ListView) rootView.findViewById(R.id.search_list);
        searchList.setAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));

        return rootView;
    }

    public void setQuery(String query) {
        this.query = query;
        setResultsString();
    }

    private void setResultsString() {
        TextView searchResultsText = (TextView) rootView.findViewById(R.id.search_results_text);
        StringBuilder builder = new StringBuilder(retriever.getStringById(R.string.base_search_results_text));
        builder.append(" \"");
        builder.append(query);
        builder.append("\" (");
        builder.append(HomeActivity.getCurrentSearchContext().getText().toString());
        builder.append(")");
        searchResultsText.setText(builder.toString());
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

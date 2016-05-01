package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;
import news.caughtup.caughtup.entities.NewsSource;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.util.StringRetriever;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class SearchFragment extends Fragment {

    private View rootView;
    private StringRetriever retriever;
    private List<ICaughtUpItem> dataArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle args = getArguments();
        retriever = StringRetriever.getInstance();

        // Set up Results String
        makeQuery(args.getString("query"));

        // Set up Results

        return rootView;
    }

    public void makeQuery(String query) {
        TextView searchContextView = HomeActivity.getCurrentSearchContext();
        String searchContext = searchContextView.getText().toString().replace(" ", "_");
        Callback callback = getSearchCallback();
        makeSearchCall(callback, query, searchContext);
        setResultsString(query);
    }

    private void makeSearchCall(Callback callback, String query, String searchContext) {
        RestProxy proxy = RestProxy.getProxy();
        proxy.getCall(String.format("/search?keyword=%s&context=%s", query, searchContext), "", callback);
    }

    private void setResultsString(String query) {
        TextView searchResultsText = (TextView) rootView.findViewById(R.id.search_results_text);
        StringBuilder builder = new StringBuilder(retriever.getStringById(R.string.base_search_results_text));
        builder.append(" \"");
        builder.append(query);
        builder.append("\" (");
        builder.append(HomeActivity.getCurrentSearchContext().getText().toString());
        builder.append(")");
        searchResultsText.setText(builder.toString());
    }

    private Callback getSearchCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        dataArray.clear();
                        // Users
                        if(jsonObject.has("users") && jsonObject.get("users") != null) {
                            JSONArray userArray = jsonObject.getJSONArray("users");
                            for(int i = 0; i < userArray.length(); ++i) {
                                JSONObject jsonUser = userArray.getJSONObject(i);
                                User user = new User(jsonUser);
                                Users.getInstance().addToUserList(user);
                                dataArray.add(user);
                            }
                        }
                        // Articles
                        if(jsonObject.has("articles") && jsonObject.get("articles") != null) {
                            JSONArray articleArray = jsonObject.getJSONArray("articles");
                            for(int i = 0; i < articleArray.length(); ++i) {
                                JSONObject jsonArticle = articleArray.getJSONObject(i);
                                Article article = new Article(jsonArticle);
                                dataArray.add(article);
                            }
                        }
                        // News Sources
                        if(jsonObject.has("news_sources") && jsonObject.get("news_sources") != null) {
                            JSONArray newsSourceArray = jsonObject.getJSONArray("news_sources");
                            for(int i = 0; i < newsSourceArray.length(); ++i) {
                                JSONObject jsonNewsSource = newsSourceArray.getJSONObject(i);
                                NewsSource newsSource = new NewsSource(jsonNewsSource);
                                dataArray.add(newsSource);
                            }
                        }
                        ListView searchList = (ListView) rootView.findViewById(R.id.search_list);
                        searchList.invalidateViews();
                        searchList.setAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.search_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //region mock data
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
    //endregion
}

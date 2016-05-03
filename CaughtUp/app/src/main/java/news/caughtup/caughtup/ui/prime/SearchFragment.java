package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import news.caughtup.caughtup.exception.CaughtUpExceptionFactory;
import news.caughtup.caughtup.exception.CaughtUpExceptionFactory.ExceptionType;
import news.caughtup.caughtup.exception.SearchException;
import news.caughtup.caughtup.util.StringRetriever;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class SearchFragment extends Fragment {

    private View rootView;
    private StringRetriever retriever;
    private List<ICaughtUpItem> dataArray = new ArrayList<>();
    private String query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle args = getArguments();
        retriever = StringRetriever.getInstance();

        makeQuery(args.getString("query"));

        return rootView;
    }

    public String getQuery() {
        return query;
    }

    public void makeQuery(String query) {
        this.query = query;
        if(query.split(" ").length > 1) {
            CaughtUpExceptionFactory factory = new CaughtUpExceptionFactory();
            SearchException exception = (SearchException) factory.getException(ExceptionType.Search);
            exception.fix(this);
        } else {
            TextView searchContextView = HomeActivity.getCurrentSearchContext();
            String searchContext = searchContextView.getText().toString().replace(" ", "_");
            Callback callback = getSearchCallback();
            makeSearchCall(callback, query, searchContext);
            setResultsString(query);
        }
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
}

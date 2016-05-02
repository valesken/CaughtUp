package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.app.ListFragment;
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

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.Article;
import news.caughtup.caughtup.entities.ICaughtUpItem;
import news.caughtup.caughtup.entities.NewsSource;
import news.caughtup.caughtup.entities.Resource;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class NewsFeedFragment extends Fragment {

    private View rootView;
    private ArrayList<ICaughtUpItem> dataArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);

        User currentUser = HomeActivity.getCurrentUser();
        Callback callback = getFollowingCallback();
        RestProxy proxy = RestProxy.getProxy();
        proxy.getCall(String.format("/follow?user_id=%d&type=all", currentUser.getUserId()), "", callback);

        return rootView;
    }

    private Callback getFollowingCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        User currentUser = HomeActivity.getCurrentUser();
                        currentUser.clearFollowing();
                        // Users
                        if(jsonObject.has("users") && jsonObject.get("users") != null) {
                            JSONArray userArray = jsonObject.getJSONArray("users");
                            for(int i = 0; i < userArray.length(); ++i) {
                                JSONObject jsonUser = userArray.getJSONObject(i);
                                User user = new User(jsonUser);
                                Users.getInstance().addToUserList(user);
                                currentUser.follow(user);
                            }
                        }
                        // News Sources
                        if(jsonObject.has("news_sources") && jsonObject.get("news_sources") != null) {
                            JSONArray newsSourceArray = jsonObject.getJSONArray("news_sources");
                            for(int i = 0; i < newsSourceArray.length(); ++i) {
                                JSONObject jsonNewsSource = newsSourceArray.getJSONObject(i);
                                NewsSource newsSource = new NewsSource(jsonNewsSource);
                                currentUser.follow(newsSource);
                            }
                        }

                        dataArray.clear();
                        for(Resource resource : currentUser.getFollowing()) {
                            Callback callback = getArticlesCallback();
                            RestProxy proxy = RestProxy.getProxy();
                            proxy.getCall(String.format("/articles?source=%s", resource.getName()), "", callback);
                        }
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.news_feed_following_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Callback getArticlesCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    ListView newsFeedList = (ListView) rootView.findViewById(R.id.news_feed_list);
                    TextView noArticlesTextView = (TextView) rootView.findViewById(R.id.news_feed_no_articles_text);
                    try {
                        JSONArray articlesArray = jsonObject.getJSONArray("articles");
                        for(int i = 0; i < articlesArray.length(); ++i) {
                            Article article = new Article(articlesArray.getJSONObject(i));
                            dataArray.add(article);
                            noArticlesTextView.setVisibility(View.GONE);
                        }
                        newsFeedList.setAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));
                        newsFeedList.setVisibility(View.VISIBLE);
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.news_feed_article_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

}

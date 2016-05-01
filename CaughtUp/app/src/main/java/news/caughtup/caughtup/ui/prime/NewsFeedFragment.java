package news.caughtup.caughtup.ui.prime;

import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import news.caughtup.caughtup.util.StringRetriever;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class NewsFeedFragment extends ListFragment {

    private ArrayList<ICaughtUpItem> dataArray = new ArrayList<>();
    private ArrayList<Resource> followingArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User currentUser = HomeActivity.getCurrentUser();
        Callback callback = getFollowingCallback();
        RestProxy proxy = RestProxy.getProxy();
        proxy.getCall(String.format("/follow?user_id=%d&type=all", currentUser.getUserId()), "", callback);

        setListAdapter(new CaughtUpTileAdapter(dataArray, getActivity()));
    }

    private Callback getFollowingCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        followingArray.clear();
                        // Users
                        if(jsonObject.has("users") && jsonObject.get("users") != null) {
                            JSONArray userArray = jsonObject.getJSONArray("users");
                            for(int i = 0; i < userArray.length(); ++i) {
                                JSONObject jsonUser = userArray.getJSONObject(i);
                                User user = new User(jsonUser);
                                Users.getInstance().addToUserList(user);
                                followingArray.add(user);
                            }
                        }
                        // News Sources
                        if(jsonObject.has("news_sources") && jsonObject.get("news_sources") != null) {
                            JSONArray newsSourceArray = jsonObject.getJSONArray("news_sources");
                            for(int i = 0; i < newsSourceArray.length(); ++i) {
                                JSONObject jsonNewsSource = newsSourceArray.getJSONObject(i);
                                NewsSource newsSource = new NewsSource(jsonNewsSource);
                                followingArray.add(newsSource);
                            }
                        }

                        // For Testing
                        for(Resource resource : followingArray) {
                            Log.e("Following Array item", resource.getName());
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

    // For demonstration only
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

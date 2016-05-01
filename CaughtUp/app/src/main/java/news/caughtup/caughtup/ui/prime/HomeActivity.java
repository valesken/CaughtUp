package news.caughtup.caughtup.ui.prime;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.fabric.sdk.android.Fabric;
import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.ui.login.LoginActivity;
import news.caughtup.caughtup.util.Constants;
import news.caughtup.caughtup.util.StringRetriever;

public class HomeActivity extends AppCompatActivity {

    private static FragmentManager fm;
    private static Toolbar myToolbar;
    private static Stack<String> myToolbarTitles;
    private static Fragment currentFragment;
    private static User currentUser;
    private static List<TextView> searchContexts;
    private static TextView currentSearchContext;

    //region public static methods
    public static void executeTransaction(Fragment fragment, String tag, int title_id) {
        executeTransaction(fragment, tag, StringRetriever.getInstance().getStringById(title_id));
    }

    public static void executeTransaction(Fragment fragment, String tag, String title) {
        currentFragment = fragment;
        setToolbarTitle(title);
        fm.beginTransaction()
                .replace(R.id.home_main_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    private static void setToolbarTitle(String title) {
        if(myToolbar != null) {
            TextView titleTextView = (TextView) myToolbar.findViewById(R.id.home_toolbar_title);
            titleTextView.setText(title);
            myToolbarTitles.push(title);
        }
    }
    //endregion

    //region Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StringRetriever.initializeStringRetriever(getResources(), Constants.IS_LOCAL);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_api_key),
                getResources().getString(R.string.twitter_api_secret));
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());

        // Set up toolbar
        myToolbarTitles = new Stack<>();
        myToolbar = (Toolbar) findViewById(R.id.home_action_bar);
        setSupportActionBar(myToolbar);
        if (myToolbar != null) {
            myToolbar.setNavigationIcon(R.drawable.profile_icon);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* User selected to go to their profile page, create the edit profile fragment */
                    EditProfileFragment editProfileFragment = new EditProfileFragment();
                    executeTransaction(editProfileFragment, "edit_profile", R.string.edit_profile_title);
                }
            });
        }
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set up search context bar
        searchContexts = new ArrayList<>();
        TextView allTextView = (TextView) findViewById(R.id.search_context_all);
        if (allTextView != null) {
            allTextView.setBackground(getDrawable(R.drawable.search_context_selected));
            currentSearchContext = allTextView;
        }
        setUpSearchContext(allTextView);
        TextView usersTextView = (TextView) findViewById(R.id.search_context_users);
        setUpSearchContext(usersTextView);
        TextView articlesTextView = (TextView) findViewById(R.id.search_context_articles);
        setUpSearchContext(articlesTextView);
        TextView newsSourcesTextView = (TextView) findViewById(R.id.search_context_news);
        setUpSearchContext(newsSourcesTextView);

        // Add new fragment
        fm = getFragmentManager();
        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
        executeTransaction(newsFeedFragment, "newsfeed", R.string.news_feed_title);

        // Get current user
        setUpCurrentUser();

        // FOR TESTING ONLY
        setUpTestUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final TextView titleTextView = (TextView) myToolbar.findViewById(R.id.home_toolbar_title);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);

        //region search icon
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_icon));
        final LinearLayout searchContextBar = (LinearLayout) findViewById(R.id.home_search_context_bar);
        final FrameLayout container = (FrameLayout) findViewById(R.id.home_main_container);

        // Detect when Search Icon clicked
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleTextView.setVisibility(View.GONE);
                searchContextBar.animate().translationY(searchContextBar.getHeight());
                container.animate().translationY(searchContextBar.getHeight());
            }
        });

        // Detect when closing Search
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleTextView.setVisibility(View.VISIBLE);
                searchContextBar.animate().translationY(0);
                container.animate().translationY(0);
                return false;
            }
        });

        // Detect when query entered
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Clear query so as not to log both KEY_UP and KEY_DOWN events (would trigger this listener twice)
                searchView.setIconified(true);

                String currentFragmentName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
                if (!currentFragmentName.equals("search")) {
                    // Pass query to SearchFragment
                    SearchFragment sf = new SearchFragment();
                    Bundle args = new Bundle();
                    args.putString("query", query);
                    sf.setArguments(args);

                    // Launch new Fragment
                    executeTransaction(sf, "search", R.string.search_title);
                } else {
                    ((SearchFragment) currentFragment).setQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //endregion

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                // User chose the "Search" item, show the search fragment
                executeTransaction(new SearchFragment(), "search", R.string.search_title);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
            if(myToolbarTitles.size() > 1) { // Safety check - better to show wrong title than crash the app
                myToolbarTitles.pop();
                String next = myToolbarTitles.pop();
                setToolbarTitle(next);
            }
        } else {
            super.onBackPressed();
        }
    }
    //endregion

    //region public accessors
    public static User getCurrentUser() {
        return currentUser;
    }

    public static TextView getCurrentSearchContext() {
        return currentSearchContext;
    }
    //endregion

    //region private helper methods
    private void setUpSearchContext(final TextView searchContext) {
        searchContexts.add(searchContext);
        searchContext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentSearchContext = searchContext;
                for (TextView textView : searchContexts) {
                    if(textView.equals(searchContext)) {
                        textView.setBackground(getDrawable(R.drawable.search_context_selected));
                    } else {
                        textView.setBackground(getDrawable(R.drawable.search_context_normal));
                    }
                }
                return false;
            }
        });
    }

    private void setUpCurrentUser() {
        try {
            Intent intent = getIntent();
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("user"));
            String username = jsonObject.getString("username");
            currentUser = new User(username);
        } catch (JSONException ignored) {
            Log.e("JSONException", "Response was not properly formatted. Returning to Login Activity.");
            Intent returnIntent = new Intent(this, LoginActivity.class);
            startActivity(returnIntent);
            finish(); // Don't stack this activity behind the login activity
        }
    }

    private void setUpTestUsers() {
        Users users = Users.getInstance();
        User user1 = new User("bjonesy");
        user1.setProfileImageId(R.mipmap.profile_pic_1);
        user1.setFullName("Bob Jones");
        user1.setGender('M');
        user1.setAge(28);
        user1.setLocation("San Mateo, CA");
        user1.setAboutMe("I'm kind of an awesome guy.");
        users.addToUserList(user1);
        User user2 = new User("whatagal");
        user2.setProfileImageId(R.mipmap.profile_pic_2);
        user2.setFullName("Sarah Doe");
        user2.setGender('F');
        user2.setAge(24);
        user2.setLocation("Mountain View, CA");
        user2.setAboutMe("Who am I? Who are you? Why are you asking so many questions?");
        users.addToUserList(user2);
        user1.addFollower(user2);
    }
    //endregion
}

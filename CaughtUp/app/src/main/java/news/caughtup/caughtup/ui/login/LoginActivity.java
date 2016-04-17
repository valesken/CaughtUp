package news.caughtup.caughtup.ui.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import news.caughtup.caughtup.R;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "a0y2v6pCXfyvHgf6HUU8KPdaJ";
    private static final String TWITTER_SECRET = "qZOUonwGdHEdxtXsrbWSO4HU444uE9r3VFB3fifQriHTauUz2D";

    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        setContentView(R.layout.activity_login);

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("just setting up my Fabric.");
        builder.show();

        fm = getFragmentManager();

        LoginFragment loginFragment = new LoginFragment();
        executeTransaction(loginFragment, "login");
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public static void executeTransaction(Fragment fragment, String tag) {
        fm.beginTransaction()
                .replace(R.id.login_container, fragment)
                .addToBackStack(tag)
                .commit();
    }
}

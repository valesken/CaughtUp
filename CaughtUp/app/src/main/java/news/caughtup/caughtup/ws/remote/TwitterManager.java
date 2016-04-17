package news.caughtup.caughtup.ws.remote;

import android.app.Activity;
import android.util.Log;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import news.caughtup.caughtup.entities.Article;

public class TwitterManager implements ISocialMediaManager {
    @Override
    public void authenticate() {
        //TODO
    }

    @Override
    public void share(String message, Article article) {
        //TODO
    }

    @Override
    public void share(String message, Article article, Activity activity) {
        TweetComposer.Builder builder = new TweetComposer.Builder(activity)
                .text(message);
        builder.show();
    }
}

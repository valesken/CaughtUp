package news.caughtup.caughtup.ws.remote;

import android.app.Activity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import news.caughtup.caughtup.entities.Article;

/**
 * TwitterManager takes care of sharing articles on Twitter through a share dialog.
 */
public class TwitterManager implements ISocialMediaManager {
    @Override
    public void share(String message, Article article, Activity activity) {
        TweetComposer.Builder builder = new TweetComposer.Builder(activity)
                .text(message + " @caughtupnews\n" + article.getArticleURI().toString());
        builder.show();
    }
}

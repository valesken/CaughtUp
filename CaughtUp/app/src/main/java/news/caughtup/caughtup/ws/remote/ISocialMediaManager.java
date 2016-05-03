package news.caughtup.caughtup.ws.remote;

import android.app.Activity;

import news.caughtup.caughtup.entities.Article;

/**
 * Interface to define Social Media Manager methods to share articles on social media.
 */
public interface ISocialMediaManager {
    void share(String message, Article article, Activity activity);
}

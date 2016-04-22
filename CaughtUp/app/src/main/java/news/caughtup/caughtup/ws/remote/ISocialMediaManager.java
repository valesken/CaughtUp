package news.caughtup.caughtup.ws.remote;

import android.app.Activity;

import news.caughtup.caughtup.entities.Article;

public interface ISocialMediaManager {
    void share(String message, Article article, Activity activity);
}

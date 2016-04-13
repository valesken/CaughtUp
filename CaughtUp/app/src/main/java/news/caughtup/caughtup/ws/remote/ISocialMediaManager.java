package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.entities.Article;

public interface ISocialMediaManager {
    void authenticate();
    void share(String message, Article article);
}

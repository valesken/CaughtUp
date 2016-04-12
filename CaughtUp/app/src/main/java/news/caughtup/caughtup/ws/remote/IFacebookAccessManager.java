package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.model.Article;

public interface IFacebookAccessManager {
    void authenticate();
    void share(String message, Article article);
}

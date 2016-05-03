package news.caughtup.caughtup.ws.remote;

import android.app.Activity;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import news.caughtup.caughtup.entities.Article;

/**
 * FacebookManager takes care of sharing articles to Facebook through a share dialog.
 */
public class FacebookManager implements ISocialMediaManager {
    @Override
    public void share(String message, Article article, Activity activity) {
        final ShareDialog shareDialog = new ShareDialog(activity);
        if(ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(article.getTitle())
                    .setContentUrl(article.getArticleURI())
                    .setContentDescription(article.getSummary())
                    .build();
            shareDialog.show(linkContent);
        }
    }
}

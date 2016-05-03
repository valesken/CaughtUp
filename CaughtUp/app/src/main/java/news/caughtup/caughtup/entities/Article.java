package news.caughtup.caughtup.entities;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;

/**
 * The Article class is used to hold information about a single article.
 */
public class Article implements ICaughtUpItem {
    private int articleId;
    private int resourceId;
    private String title;
    private String date;
    private int thumbnailID;
    private String summary;
    private Uri articleURI;

    /**
     * Constructor for Artcile.
     * @param jsonObject
     * @throws JSONException
     */
    public Article(JSONObject jsonObject) throws JSONException {
        articleId = jsonObject.getInt("articleId");
        resourceId = jsonObject.getInt("resourceId");
        title = jsonObject.getString("title");
        date = jsonObject.getString("date");
        summary = jsonObject.getString("summary");
        articleURI = Uri.parse(jsonObject.getString("articleURI"));
        thumbnailID = (articleURI.toString().contains("www.bbc.")) ? R.mipmap.bbc_icon : R.mipmap.cnn_icon;
    }

    /*
    Geters and Setters.
     */
    public int getArticleId() {
        return articleId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getThumbnailID() {
        return thumbnailID;
    }

    public void setThumbnailID(int image) {
        this.thumbnailID = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Uri getArticleURI() {
        return articleURI;
    }

    public void setArticleURI(Uri articleURI) {
        this.articleURI = articleURI;
    }
}

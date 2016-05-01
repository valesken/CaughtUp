package news.caughtup.caughtup.entities;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;


public class Article implements ICaughtUpItem {
    private int articleId;
    private int resourceId;
    private String title;
    private String date;
    private int thumbnailID = R.mipmap.bbc_icon; //TODO: Get actual corresponding thumbnail
    private String summary;
    private Uri articleURI;

    public Article(String title, String date, int thumbnailID, String summary, Uri articleURI) {
        this.title = title;
        this.date = date;
        this.thumbnailID = thumbnailID;
        this.summary = summary;
        this.articleURI = articleURI;
    }

    public Article(JSONObject jsonObject) throws JSONException {
        articleId = jsonObject.getInt("articleId");
        resourceId = jsonObject.getInt("resourceId");
        title = jsonObject.getString("title");
        date = jsonObject.getString("date");
        summary = jsonObject.getString("summary");
        articleURI = Uri.parse(jsonObject.getString("articleURI"));
    }

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

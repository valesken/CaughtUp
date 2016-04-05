package news.caughtup.caughtup.model;

import android.net.Uri;


public class Article {
    private String title;
    private String date;
    private int thumbnailID;
    private String summary;
    private Uri articleURI;

    public Article(String title, String date, int thumbnailID, String summary, Uri articleURI) {
        this.title = title;
        this.date = date;
        this.thumbnailID = thumbnailID;
        this.summary = summary;
        this.articleURI = articleURI;
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

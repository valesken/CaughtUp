package news.caughtup.caughtup.entities;

import android.net.Uri;

public class NewsSource extends Resource implements ICaughtUpItem {

    private String baseUrl;
    private String description;
    private int thumbnailId;

    // Constructor
    public NewsSource(String name, String baseUrl, int thumbnailId, String description) {
        super(name);
        this.baseUrl = baseUrl;
        this.thumbnailId = thumbnailId;
        this.description = description;
    }

    // Setters
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters
    public Uri getBaseUrl() {
        return Uri.parse(baseUrl);
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public String getDescription() {
        return description;
    }
}

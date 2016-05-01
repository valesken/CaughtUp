package news.caughtup.caughtup.entities;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;

public class NewsSource extends Resource implements ICaughtUpItem {

    private int resourceId;
    private String baseUrl;
    private String description;
    private int thumbnailId = R.mipmap.bbc_icon; // TODO: Get actual corresponding icon

    //region Constructors
    public NewsSource(String name, String baseUrl, int thumbnailId, String description) {
        super(name);
        this.baseUrl = baseUrl;
        this.thumbnailId = thumbnailId;
        this.description = description;
    }

    public NewsSource(JSONObject jsonObject) throws JSONException {
        super(jsonObject.getString("name"));
        resourceId = jsonObject.getInt("resourceId");
        baseUrl = jsonObject.getString("baseURL");
        description = jsonObject.getString("description");
    }
    //endregion

    //region Setters
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion

    //region Getters
    public int getResourceId() {
        return resourceId;
    }

    public Uri getBaseUrl() {
        return Uri.parse(baseUrl);
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public String getDescription() {
        return description;
    }
    //endregion
}

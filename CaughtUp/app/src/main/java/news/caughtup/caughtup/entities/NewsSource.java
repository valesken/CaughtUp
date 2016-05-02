package news.caughtup.caughtup.entities;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;

public class NewsSource extends Resource implements ICaughtUpItem {

    private String baseUrl;
    private String description;
    private int thumbnailId;

    //region Constructors
    public NewsSource(JSONObject jsonObject) throws JSONException {
        super(jsonObject.getString("name"));
        setResourceId(jsonObject.getInt("resourceId"));
        baseUrl = jsonObject.getString("baseURL");
        description = jsonObject.getString("description");
        thumbnailId = (name.toLowerCase().equals("cnn")) ? R.mipmap.cnn_icon : R.mipmap.bbc_icon;
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

package news.caughtup.caughtup.entities;

import org.json.JSONObject;

public class ResponseObject {
    private int responseCode;
    private JSONObject jsonObject;

    public ResponseObject(int responseCode, JSONObject jsonObject) {
        this.responseCode = responseCode;
        this.jsonObject = jsonObject;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

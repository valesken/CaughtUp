package news.caughtup.caughtup.entities;

import org.json.JSONObject;

/**
 * Helper class to wrap the response JSON and HTTP status of a HTTP response from the backend.
 */
public class ResponseObject {
    private int responseCode;
    private JSONObject jsonObject;

    /**
     * Constructor.
     * @param responseCode
     * @param jsonObject
     */
    public ResponseObject(int responseCode, JSONObject jsonObject) {
        this.responseCode = responseCode;
        this.jsonObject = jsonObject;
    }

    /*
    Getters and Setters.
     */
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

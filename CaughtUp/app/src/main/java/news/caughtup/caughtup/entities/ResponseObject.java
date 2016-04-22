package news.caughtup.caughtup.entities;

public class ResponseObject {
    private int responseCode;
    private Object jsonObject;

    public ResponseObject(int responseCode, Object jsonObject) {
        this.responseCode = responseCode;
        this.jsonObject = jsonObject;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Object getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(Object jsonObject) {
        this.jsonObject = jsonObject;
    }
}

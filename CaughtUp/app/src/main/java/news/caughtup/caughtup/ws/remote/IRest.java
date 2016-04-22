package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.entities.Callback;

public interface IRest {
    void getCall(String endPoint, String jsonData, Callback callback);
    void postCall(String endPoint, String jsonData, Callback callback);
    void putCall(String endPoint, String jsonData, Callback callback);
    void deleteCall(String endPoint, String jsonData, Callback callback);
}

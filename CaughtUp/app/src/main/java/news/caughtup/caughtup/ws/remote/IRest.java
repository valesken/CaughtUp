package news.caughtup.caughtup.ws.remote;

/**
 * Interface used to define HTTP REST operations to be implemented by REST clients.
 */
public interface IRest {
    void getCall(String endPoint, String jsonData, Callback callback);
    void postCall(String endPoint, String jsonData, Callback callback);
    void putCall(String endPoint, String jsonData, Callback callback);
    void deleteCall(String endPoint, String jsonData, Callback callback);
}

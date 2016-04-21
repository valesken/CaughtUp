package news.caughtup.caughtup.ws.remote;

public interface IRest {
    Object getCall(String endPoint, Class c);
    void postCall(String endPoint, Class c);
    void putCall(String endPoint, Class c);
    void deleteCall(String endPoint, Class c);
}

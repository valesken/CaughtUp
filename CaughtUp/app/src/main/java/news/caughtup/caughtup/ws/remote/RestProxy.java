package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.entities.Callback;
import news.caughtup.caughtup.entities.ResponseObject;

public class RestProxy implements IRest {
    private static RestProxy proxy;
    private static RestClient client;

    private RestProxy() {}

    public static RestProxy getProxy() {
        if (proxy == null) {
            proxy = new RestProxy();
            client = new RestClient();
        }
        return proxy;
    }

    @Override
    public void getCall(String endPoint, String jsonData, Callback callback) {
        client.getCall(endPoint, jsonData, callback);
    }

    @Override
    public void postCall(String endPoint, String jsonData, Callback callback) {
        client.postCall(endPoint, jsonData, callback);
    }

    @Override
    public void putCall(String endPoint, String jsonData, Callback callback) {
        client.putCall(endPoint, jsonData, callback);
    }

    @Override
    public void deleteCall(String endPoint, String jsonData, Callback callback) {
        client.deleteCall(endPoint, jsonData, callback);
    }
}

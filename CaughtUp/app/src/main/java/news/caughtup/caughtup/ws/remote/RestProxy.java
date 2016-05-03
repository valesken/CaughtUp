package news.caughtup.caughtup.ws.remote;

/**
 * A proxy for REST calls.
 */
public class RestProxy implements IRest {
    private static RestProxy proxy;
    private static RestClient client;

    private RestProxy() {}

    /**
     * Returns a singleton of the class.
     * @return
     */
    public static RestProxy getProxy() {
        if (proxy == null) {
            proxy = new RestProxy();
            client = new RestClient();
        }
        return proxy;
    }

    /*
    REST proxy methods
     */
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

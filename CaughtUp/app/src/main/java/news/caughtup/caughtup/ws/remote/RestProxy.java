package news.caughtup.caughtup.ws.remote;

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
    public Object getCall(String endPoint, Class c) {
        return client.getCall(endPoint, c);
    }

    @Override
    public void postCall(String endPoint, Class c) {
        client.postCall(endPoint, c);
    }

    @Override
    public void putCall(String endPoint, Class c) {
        client.putCall(endPoint, c);
    }

    @Override
    public void deleteCall(String endPoint, Class c) {
        client.deleteCall(endPoint, c);
    }
}

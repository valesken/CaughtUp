package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.entities.ResponseObject;

public abstract class Callback {
    public abstract void process(ResponseObject responseObject);
}

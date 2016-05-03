package news.caughtup.caughtup.ws.remote;

import news.caughtup.caughtup.entities.ResponseObject;

/**
 * Abstract callback class to use with handling HTTP responses.
 */
public abstract class Callback {
    public abstract void process(ResponseObject responseObject);
}

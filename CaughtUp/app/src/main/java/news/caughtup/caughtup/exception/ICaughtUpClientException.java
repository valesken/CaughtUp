package news.caughtup.caughtup.exception;

public interface ICaughtUpClientException {
    void fix(Object objectToFix);
    void log(String message);
}

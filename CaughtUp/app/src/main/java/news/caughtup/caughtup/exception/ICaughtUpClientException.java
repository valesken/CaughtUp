package news.caughtup.caughtup.exception;

public interface ICaughtUpClientException {
    void fix(int exceptionID);
    void log();
}

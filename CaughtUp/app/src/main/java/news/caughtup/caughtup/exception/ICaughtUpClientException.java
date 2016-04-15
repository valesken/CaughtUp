package news.caughtup.caughtup.exception;

public interface ICaughtUpClientException {
    public void fix(int exceptionID);
    public void log();
}

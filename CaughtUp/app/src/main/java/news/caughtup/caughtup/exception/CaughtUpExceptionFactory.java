package news.caughtup.caughtup.exception;

public abstract class CaughtUpExceptionFactory extends Exception implements ICaughtUpClientException {
    public enum ExceptionType {
        EditProfile, NewsFeed, Search
    }

    public CaughtUpExceptionFactory createException(ExceptionType type) {
        switch (type) {
            case EditProfile:
                return new EditProfileException();
            case NewsFeed:
                return new NewsFeedException();
            case Search:
                return new SearchException();
            default:
                return null;
        }
    }
}

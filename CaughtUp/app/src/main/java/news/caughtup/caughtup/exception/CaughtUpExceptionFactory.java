package news.caughtup.caughtup.exception;

public class CaughtUpExceptionFactory {
    public enum ExceptionType {
        EditProfile, NewsFeed, Search
    }

    public ICaughtUpClientException getException(ExceptionType type) {
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

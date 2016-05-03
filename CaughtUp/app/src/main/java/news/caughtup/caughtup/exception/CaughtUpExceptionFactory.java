package news.caughtup.caughtup.exception;

public class CaughtUpExceptionFactory {
    public enum ExceptionType {
        EditProfile, Search
    }

    public ICaughtUpClientException getException(ExceptionType type) {
        switch (type) {
            case EditProfile:
                return new EditProfileException();
            case Search:
                return new SearchException();
            default:
                return null;
        }
    }
}

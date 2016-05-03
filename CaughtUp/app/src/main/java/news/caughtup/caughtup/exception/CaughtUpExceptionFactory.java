package news.caughtup.caughtup.exception;

/**
 * The Factory class that returns a specific type of CaughtUpException implementation.
 */
public class CaughtUpExceptionFactory {
    /**
     * ExceptionTypes available.
     */
    public enum ExceptionType {
        EditProfile, Search
    }

    /**
     * Returns a specific CaughtUpException subclass based on ExceptionType
     * @param type
     * @return
     */
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

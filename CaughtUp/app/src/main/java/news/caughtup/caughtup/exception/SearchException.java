package news.caughtup.caughtup.exception;

import android.util.Log;
import news.caughtup.caughtup.ui.prime.SearchFragment;

/**
 * Search Exception class for handling search faults.
 */
public class SearchException implements ICaughtUpClientException {

    /**
     * Fault-tolerance method to fix raised SearchExceptions
     * @param objectToFix
     */
    @Override
    public void fix(Object objectToFix) {
        if(objectToFix instanceof SearchFragment) {
            String[] query = ((SearchFragment) objectToFix).getQuery().split(" ");
            ((SearchFragment) objectToFix).makeQuery(query[0]);
            log("You can only search for 1 keyword at a time.");
        }
    }

    /**
     * Logging method for debugging purposes.
     * @param message
     */
    @Override
    public void log(String message) {
        Log.e("SearchException", message);
    }
}

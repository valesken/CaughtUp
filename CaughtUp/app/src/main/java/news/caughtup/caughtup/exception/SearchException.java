package news.caughtup.caughtup.exception;

import android.util.Log;

import news.caughtup.caughtup.ui.prime.SearchFragment;

public class SearchException implements ICaughtUpClientException {

    @Override
    public void fix(Object objectToFix) {
        if(objectToFix instanceof SearchFragment) {
            String[] query = ((SearchFragment) objectToFix).getQuery().split(" ");
            ((SearchFragment) objectToFix).makeQuery(query[0]);
            log("You can only search for 1 keyword at a time.");
        }
    }

    @Override
    public void log(String message) {
        Log.e("SearchException", message);
    }
}

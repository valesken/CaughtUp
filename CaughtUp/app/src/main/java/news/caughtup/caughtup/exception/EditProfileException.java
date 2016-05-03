package news.caughtup.caughtup.exception;

import android.util.Log;
import android.widget.EditText;

/**
 * Exception for edit profile faults.
 */
public class EditProfileException implements ICaughtUpClientException {

    /**
     * Fault-tolerance method to fix the raised exception.
     * @param objectToFix
     */
    @Override
    public void fix(Object objectToFix) {
        if(objectToFix instanceof EditText) {
            EditText ageEditText = (EditText) objectToFix;
            ageEditText.setText("0");
            log("When editing your profile, the age is required.");
        }
    }

    /**
     * Logging method for debugging purposes.
     * @param message
     */
    @Override
    public void log(String message) {
        Log.e("EditProfileException", message);
    }
}

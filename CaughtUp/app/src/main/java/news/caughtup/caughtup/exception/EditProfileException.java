package news.caughtup.caughtup.exception;

import android.util.Log;
import android.widget.EditText;

public class EditProfileException implements ICaughtUpClientException {

    @Override
    public void fix(Object objectToFix) {
        if(objectToFix instanceof EditText) {
            EditText ageEditText = (EditText) objectToFix;
            ageEditText.setText("0");
            log("When editing your profile, the age is required.");
        }
    }

    @Override
    public void log(String message) {
        Log.e("EditProfileException", message);
    }
}

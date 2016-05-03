package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.util.StringRetriever;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

/**
 * @author CaughtUp
 */
public class ChangePasswordFragment extends Fragment {
    private EditText oldPasswordView;
    private EditText newPasswordView;
    private EditText confirmPasswordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        final HomeActivity activity = (HomeActivity) getActivity();

        oldPasswordView = (EditText) rootView.findViewById(R.id.change_password_old_password_edit_text);
        newPasswordView = (EditText) rootView.findViewById(R.id.change_password_new_password_edit_text);
        confirmPasswordView = (EditText) rootView.findViewById(R.id.change_password_confirm_password_edit_text);

        // Set up listener for the save password button
        Button saveButton = (Button) rootView.findViewById(R.id.change_password_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordView.getText().toString();
                String confirmPassword = confirmPasswordView.getText().toString();
                // If the passwords don't match inform the user
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.passwords_should_match),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Make the REST call to update the password
                    RestProxy proxy = RestProxy.getProxy();
                    User user = HomeActivity.getCurrentUser();
                    Callback callback = getChangePasswordCallback();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("oldPassword", oldPasswordView.getText().toString());
                        jsonObject.put("newPassword", newPassword);
                        proxy.putCall(String.format("/password/%s", user.getName()), jsonObject.toString(), callback);
                    } catch (JSONException e) {
                        Log.e("JSONException", "Couldn't read input fields");
                    }
                }
            }
        });

        // Set up listener for the cancel button
        Button cancelButton = (Button) rootView.findViewById(R.id.change_password_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });

        return rootView;
    }

    /**
     * Create the callback to be invoked after the change password request has been processed
     * by the server
     * @return
     */
    private Callback getChangePasswordCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                // Handle the different responses by the server
                // by displaying the appropriate message
                if (responseObject.getResponseCode() == 404) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.user_not_found),
                            Toast.LENGTH_LONG).show();
                } else if (responseObject.getResponseCode() == 403) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.wrong_old_password),
                            Toast.LENGTH_LONG).show();
                } else if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        String responseMessage = jsonObject.getString("message");
                        if (!responseMessage.equals("Success")) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    getResources().getString(R.string.change_password_server_error),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    getResources().getString(R.string.successfull_password_change),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.change_password_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}

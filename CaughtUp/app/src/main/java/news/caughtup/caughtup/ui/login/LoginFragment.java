package news.caughtup.caughtup.ui.login;

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
import news.caughtup.caughtup.ui.prime.HomeActivity;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

/**
 * The Login fragment which allows a new user to go to Register or log in if already a member.
 */
public class LoginFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        // Detect "Register" button clicked
        Button registerButton = (Button) view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                LoginActivity.executeTransaction(registerFragment, "register");
            }
        });

        // Detect "Login" button clicked
        Button loginButton = (Button) view.findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked();
            }
        });

        return view;
    }

    /**
     * Login button method to initiate a login.
     */
    public void loginButtonClicked() {
        // Get userName and Password
        final EditText userName = (EditText) view.findViewById(R.id.login_username_edit_text);
        final EditText password = (EditText) view.findViewById(R.id.login_password_edit_text);
        String userNameString = userName.getText().toString();
        String passwordString = password.getText().toString();

        // Ignore click if userName and/or password if empty
        if(userNameString.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.empty_user_name),
                    Toast.LENGTH_LONG).show();
        } else if(passwordString.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.empty_password),
                    Toast.LENGTH_LONG).show();
        } else if(!userNameString.isEmpty() && !passwordString.isEmpty()) {
            Callback callback = getLoginCallback();
            makeLoginCall(userNameString, passwordString, callback);
        }
    }

    /**
     * Returns a login callback to handle a login HTTP response.
     * @return
     */
    public Callback getLoginCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    // Login successful!
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        // Direct the user to the home page
                        String responseUserName = jsonObject.getString("username");
                        if(responseUserName != null && !responseUserName.isEmpty()) {
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra("user", jsonObject.toString());
                            startActivity(intent);
                            getActivity().finish(); // Don't stack this activity behind the home activity
                        }
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                } else {
                    //Login failed...
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.login_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    /**
     * Makes the HTTP request for login to the backend.
     * @param username
     * @param password
     * @param callback
     */
    public void makeLoginCall(String username, String password, Callback callback) {
        RestProxy proxy = RestProxy.getProxy();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            proxy.postCall("/login/" + username, jsonObject.toString(), callback);
        } catch (JSONException e) {
            Log.e("LoginFragment", "Error trying to create JSON object");
        }
    }
}

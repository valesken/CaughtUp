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

import org.json.JSONException;
import org.json.JSONObject;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.ui.prime.HomeActivity;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

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

    public void loginButtonClicked() {
        // Get userName and Password
        final EditText userName = (EditText) view.findViewById(R.id.login_username_edit_text);
        final EditText password = (EditText) view.findViewById(R.id.login_password_edit_text);
        String userNameString = userName.getText().toString();
        String passwordString = password.getText().toString();

        // Ignore click if userName and/or password if empty
        if(!userNameString.isEmpty() && !passwordString.isEmpty()) {
            Callback callback = getLoginCallback();
            makeLoginCall(userNameString, passwordString, callback);
        }
    }

    public Callback getLoginCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    Log.d("Response Code", "We got a 200 OK!");
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        String responseUserName = jsonObject.getString("username");
                        if(responseUserName == null || responseUserName.isEmpty())
                            Log.d("Username", "Cannot be found");
                        else
                            Log.d("Username", jsonObject.get("username").toString());
                    } catch (JSONException ignored) {
                        Log.e("JSONException", "Couldn't read returned JSON");
                    } catch (NullPointerException ignored) {
                        Log.e("NullPointerException", "No JSON Object in response");
                    }
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // Don't stack this activity behind the home activity
                }
            }
        };
    }

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

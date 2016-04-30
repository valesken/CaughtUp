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
import news.caughtup.caughtup.entities.RegistrationUser;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.ui.prime.HomeActivity;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class RegisterFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = (Button) view.findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked();
            }
        });
        return view;
    }

    public void registerButtonClicked() {
        // Get userName and Password
        RegistrationUser newUser = getRegistrationUser();

        // Ignore click if userName and/or password if empty
        if(newUser.confirmName() && newUser.confirmPassword()) {
            Callback callback = getRegisterCallback();
            makeRegisterCall(newUser, callback);
        }
    }

    public RegistrationUser getRegistrationUser() {
        // Get layout components
        EditText userName = (EditText) view.findViewById(R.id.register_username_edit_text);
        EditText password = (EditText) view.findViewById(R.id.register_password_edit_text);
        EditText confirm = (EditText) view.findViewById(R.id.register_confirm_password_edit_text);
        EditText fullName = (EditText) view.findViewById(R.id.register_fullname_edit_text);
        EditText email = (EditText) view.findViewById(R.id.register_email_edit_text);

        // Set up RegistrationUser
        RegistrationUser user = new RegistrationUser(userName.getText().toString());
        user.setPassword(password.getText().toString(), confirm.getText().toString());
        user.setFullName(fullName.getText().toString());
        user.setEmail(email.getText().toString());
        return user;
    }

    public Callback getRegisterCallback() {
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

    public void makeRegisterCall(RegistrationUser user, Callback callback) {
        RestProxy proxy = RestProxy.getProxy();
        try {
            JSONObject jsonObject = user.getJSON();
            proxy.postCall("/register/" + user.getName(), jsonObject.toString(), callback);
        } catch (JSONException e) {
            Log.e("LoginFragment", "Error trying to create JSON object");
        }
    }
}

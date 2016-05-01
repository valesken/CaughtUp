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
import news.caughtup.caughtup.entities.RegistrationUser;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.ui.prime.HomeActivity;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class RegisterFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        // Detect "Register" button clicked
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
        RegistrationUser newUser = getRegistrationUser();
        if(!newUser.confirmName()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.empty_user_name),
                    Toast.LENGTH_LONG).show();
        } else if(!newUser.confirmPassword()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.register_password_error),
                    Toast.LENGTH_LONG).show();
        } else {
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
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
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
                    Toast.makeText(getActivity().getApplicationContext(),
                            getResources().getString(R.string.register_server_error),
                            Toast.LENGTH_LONG).show();
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

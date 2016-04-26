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

import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.ui.prime.HomeActivity;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button registerButton = (Button) view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                LoginActivity.executeTransaction(registerFragment, "register");
            }
        });

        final EditText userName = (EditText) view.findViewById(R.id.login_username_edit_text);
        final EditText password = (EditText) view.findViewById(R.id.login_password_edit_text);

        Button loginButton = (Button) view.findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback callback = new Callback() {
                    @Override
                    public void process(ResponseObject responseObject) {
                        if (responseObject.getResponseCode() == 200 ) {
                            JSONObject jsonObject = (JSONObject) responseObject.getJsonObject();
                            try {
                                Log.d("Username", jsonObject.get("username").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                };
                RestProxy proxy = RestProxy.getProxy();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password", password.getText().toString());
                } catch (JSONException e) {
                    Log.e("LoginFragment", "Error trying to create JSON object");
                }
                proxy.postCall("/login/dimitris", jsonObject.toString(), callback);
            }
        });

        return view;
    }
}

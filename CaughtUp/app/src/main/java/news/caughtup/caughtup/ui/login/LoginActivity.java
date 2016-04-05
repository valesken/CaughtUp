package news.caughtup.caughtup.ui.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import news.caughtup.caughtup.R;

public class LoginActivity extends AppCompatActivity {
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm = getFragmentManager();

        LoginFragment loginFragment = new LoginFragment();
        executeTransaction(loginFragment, "login");
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public static void executeTransaction(Fragment fragment, String tag) {
        fm.beginTransaction()
                .replace(R.id.login_container, fragment)
                .addToBackStack(tag)
                .commit();
    }
}

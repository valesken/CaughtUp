package news.caughtup.caughtup.ui.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import news.caughtup.caughtup.R;
import news.caughtup.caughtup.util.Constants;
import news.caughtup.caughtup.util.StringRetriever;

/**
 * The main onboarding Activity to display Login/Registration fragments to new users.
 */
public class LoginActivity extends AppCompatActivity {
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing Crashlytics for error reporting
        Fabric.with(this, new Crashlytics());

        // Initializing the fragment manager to manage login/register fragments
        fm = getFragmentManager();
        StringRetriever.initializeStringRetriever(getResources(), Constants.IS_LOCAL);

        // Display the login fragment
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

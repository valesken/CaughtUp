package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import news.caughtup.caughtup.R;

public class HomeActivity extends AppCompatActivity {
    private static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fm = getFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_action_bar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.profile_icon);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* User selected to go to their profile page, create the edit profile fragment */
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                executeTransaction(editProfileFragment, "edit_profile");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                // User chose the "Search" item, show the search fragment
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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
                .replace(R.id.home_main_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    public static void restorePreviousFragment() {
        fm.popBackStack();
    }
}

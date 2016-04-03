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
import news.caughtup.caughtup.model.User;
import news.caughtup.caughtup.model.UserList;

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

        // FOR TESTING ONLY
        //testPublicProfile();
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

    private void testPublicProfile() {
        //region setup Users
        User user1 = new User("bjonesy");
        user1.setProfileImageId(R.mipmap.profile_pic_1);
        user1.setFullName("Bob Jones");
        user1.setGender('M');
        user1.setAge(28);
        user1.setLocation("San Mateo, CA");
        user1.setAboutMe("I'm kind of an awesome guy.");
        UserList.addToUserList(user1);
        User user2 = new User("whatagal");
        user2.setProfileImageId(R.mipmap.profile_pic_2);
        user2.setFullName("Sarah Doe");
        user2.setGender('F');
        user2.setAge(24);
        user2.setLocation("Mountain View, CA");
        user2.setAboutMe("Who am I? Who are you? Why are you asking so many questions?");
        UserList.addToUserList(user2);
        user1.addFollower(user2);
        //endregion

        PublicProfileFragment publicProfile = new PublicProfileFragment();
        Bundle args = new Bundle();
        args.putString("user", user1.getUserName());
        publicProfile.setArguments(args);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.main_container, publicProfile)
                .addToBackStack("publicProfile")
                .commit();

    }

    public static void restorePreviousFragment() {
        fm.popBackStack();
    }
}

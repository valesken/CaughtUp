package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.model.User;
import news.caughtup.caughtup.model.UserList;

public class SearchFragment extends Fragment{

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity().getApplicationContext();
        LinearLayout searchLayout = (LinearLayout) rootView.findViewById(R.id.search_layout);
        addAllUsers(searchLayout);
        return rootView;
    }

    private void addAllUsers(LinearLayout layout) {
        for(User user : UserList.getUsers()) {
            addUser(user, layout);
        }
    }

    private void addUser(final User user, LinearLayout layout) {
        RelativeLayout userResult = new RelativeLayout(context);
        userResult.setPadding(5, 5, 5, 5);
        Random random = new Random();

        //region profile picture
        ImageView profilePic = new ImageView(context);
        profilePic.setId(random.nextInt(Integer.MAX_VALUE));
        profilePic.setImageDrawable(getResources().getDrawable(user.getProfileImageId(), null));
        profilePic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadUser(user);
                return false;
            }
        });
        RelativeLayout.LayoutParams profilePicParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        profilePicParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profilePicParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        userResult.addView(profilePic, profilePicParams);
        //endregion

        //region follow/unfollow button
        final ImageButton imageButton = new ImageButton(context);
        imageButton.setId(random.nextInt(Integer.MAX_VALUE));
        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.add_icon, null));
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setElevation(3);
        imageButton.setScaleType(ImageView.ScaleType.FIT_END);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable add = getResources().getDrawable(R.drawable.add_icon, null);
                if (imageButton.getDrawable().getConstantState().equals(add.getConstantState())) {
                    Toast.makeText(context,
                            String.format("Now following %s!", user.getUserName()),
                            Toast.LENGTH_SHORT).show();
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.unfollow_icon, null));
                } else {
                    Toast.makeText(context,
                            String.format("No longer following %s.", user.getUserName()),
                            Toast.LENGTH_SHORT).show();
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.add_icon, null));
                }
            }
        });
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        userResult.addView(imageButton, imageButtonParams);
        //endregion

        //region user info
        LinearLayout userInfo = new LinearLayout(context);
        userInfo.setOrientation(LinearLayout.VERTICAL);
        userInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                loadUser(user);
                return false;
            }
        });

        TextView userName = new TextView(context);
        userName.setText(user.getUserName());
        userName.setTypeface(null, Typeface.BOLD);
        userName.setTextColor(Color.BLACK);
        userInfo.addView(userName,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView aboutMe = new TextView(context);
        aboutMe.setTextColor(Color.BLACK);
        String aboutMeString = user.getAboutMe();
        if(aboutMeString.length() > 140) {
            aboutMeString = aboutMeString.substring(0, 140) + "...";
        }
        aboutMe.setText(aboutMeString);
        userInfo.addView(aboutMe,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams userInfoParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        userInfoParams.addRule(RelativeLayout.RIGHT_OF, profilePic.getId());
        userInfoParams.addRule(RelativeLayout.LEFT_OF, imageButton.getId());

        userResult.addView(userInfo, userInfoParams);
        //endregion

        // Add User result to overall layout
        layout.addView(userResult,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void loadUser(User user) {
        PublicProfileFragment publicProfile = new PublicProfileFragment();
        Bundle args = new Bundle();
        args.putString("user", user.getUserName());
        publicProfile.setArguments(args);
        HomeActivity.executeTransaction(publicProfile, user.getUserName());
    }
}

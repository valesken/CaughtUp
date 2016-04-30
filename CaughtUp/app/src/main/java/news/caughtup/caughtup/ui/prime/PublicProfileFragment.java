package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;

public class PublicProfileFragment extends Fragment {

    private Context context;
    private User user;
    private GridLayout followersGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_public_profile, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        context = activity.getApplicationContext();
        Bundle args = getArguments();
        user = Users.getInstance().getUser(args.getString("user"));

        List<User> followers = user != null ? user.getFollowers() : null;
        String followersMsg = String.format("You have %d followers", followers != null ? followers.size() : 0);

        //region get layout elements
        ImageView userProfilePic = (ImageView) rootView.findViewById(R.id.public_profile_user_picture_image_view);
        TextView userName = (TextView) rootView.findViewById(R.id.public_profile_username_text_view);
        TextView userGender = (TextView) rootView.findViewById(R.id.public_profile_gender_text_view);
        TextView userAge = (TextView) rootView.findViewById(R.id.public_profile_age_text_view);
        TextView userFullName = (TextView) rootView.findViewById(R.id.public_profile_full_name_text_view);
        TextView userLocation = (TextView) rootView.findViewById(R.id.public_profile_location_text_view);
        TextView userAboutMe = (TextView) rootView.findViewById(R.id.public_profile_about_me_text_view);
        final Button userFollowButton = (Button) rootView.findViewById(R.id.public_profile_follow_me_button);
        TextView followersTextView = (TextView) rootView.findViewById(R.id.public_profile_followers_text_view);
        followersGrid = (GridLayout) rootView.findViewById(R.id.public_profile_followers_grid);
        //endregion

        //region set contents of layout elements
        Drawable userProfileDrawable = getResources().getDrawable(user.getProfileImageId(), null);
        userProfilePic.setImageDrawable(userProfileDrawable);
        userName.setText(user.getName());
        userGender.setText(Character.toString(user.getGender()));
        userAge.setText(String.format("%d", user.getAge()));
        userFullName.setText(user.getFullName());
        userLocation.setText(user.getLocation());
        userAboutMe.setText(user.getAboutMe());
        followersTextView.setText(followersMsg);
        if(followers != null) {
            for (User user : followers) {
                addFollower(user);
            }
        }
        //endregion

        //region onClickListener
        userFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userFollowButton.getText().toString().equals(getResources().getString(R.string.follow_me_button_text)
                )) {
                    Toast.makeText(context,
                            String.format("Now following %s!", user.getName()),
                            Toast.LENGTH_SHORT).show();
                    userFollowButton.setText(getResources().getString(R.string.unfollow_button_text));
                } else {
                    Toast.makeText(context,
                            String.format("Stopped following %s.", user.getName()),
                            Toast.LENGTH_SHORT).show();
                    userFollowButton.setText(getResources().getString(R.string.follow_me_button_text));
                }
            }
        });
        //endregion

        return rootView;
    }

    private void addFollower(User follower) {
        LinearLayout followerLayout = new LinearLayout(context);
        followerLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView profilePic = new ImageView(context);
        Drawable profilePicDrawable = getResources().getDrawable(follower.getProfileImageId(), null);
        profilePic.setImageDrawable(profilePicDrawable);
        followerLayout.addView(profilePic,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView userName = new TextView(context);
        userName.setTextColor(Color.BLACK);
        userName.setText(follower.getName());
        followerLayout.addView(userName,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        followersGrid.addView(followerLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}

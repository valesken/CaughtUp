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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.entities.Users;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class PublicProfileFragment extends Fragment {

    private View rootView;
    private Context context;
    private User user;
    private GridLayout followersGrid;
    private RestProxy proxy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_public_profile, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        context = activity.getApplicationContext();
        Bundle args = getArguments();

        proxy = RestProxy.getProxy();
        String username = args.getString("username");
        Callback callback = getUserCallback();
        proxy.getCall(String.format("/profile/%s", username), "", callback);

        return rootView;
    }

    private Callback getUserCallback() {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    JSONObject jsonObject = responseObject.getJsonObject();
                    try {
                        user = new User(jsonObject.getJSONObject("profile"));
                        JSONArray followers = jsonObject.getJSONArray("followers");
                        for(int i = 0; i < followers.length(); ++i) {
                            user.addFollower(new User(followers.getJSONObject(i)));
                        }
                        setUpUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getResources().getString(R.string.get_user_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setUpUser() {
        if(user != null) {
            List<User> followers = user.getFollowers();
            String followersMsg = String.format("%s has %d followers",
                    user.getName(),
                    followers != null ? followers.size() : 0);

            // UserName
            TextView userName = (TextView) rootView.findViewById(R.id.public_profile_username_text_view);
            userName.setText(user.getName());

            // Gender
            TextView userGender = (TextView) rootView.findViewById(R.id.public_profile_gender_text_view);
            userGender.setText(Character.toString(user.getGender()));

            // Age
            TextView userAge = (TextView) rootView.findViewById(R.id.public_profile_age_text_view);
            userAge.setText(String.format("%d", user.getAge()));

            // Full Name
            TextView userFullName = (TextView) rootView.findViewById(R.id.public_profile_full_name_text_view);
            userFullName.setText(user.getFullName());

            // Location
            TextView userLocation = (TextView) rootView.findViewById(R.id.public_profile_location_text_view);
            userLocation.setText(user.getLocation());

            // About Me
            TextView userAboutMe = (TextView) rootView.findViewById(R.id.public_profile_about_me_text_view);
            userAboutMe.setText(user.getAboutMe());

            // Profile Picture
            ImageView userProfilePic = (ImageView) rootView.findViewById(R.id.public_profile_user_picture_image_view);
            Drawable userProfileDrawable = getResources().getDrawable(user.getProfileImageId(), null);
            userProfilePic.setImageDrawable(userProfileDrawable);

            // Followers
            TextView followersTextView = (TextView) rootView.findViewById(R.id.public_profile_followers_text_view);
            followersGrid = (GridLayout) rootView.findViewById(R.id.public_profile_followers_grid);
            followersTextView.setText(followersMsg);
            if (followers != null) {
                for (User follower : followers) {
                    addFollower(follower);
                }
            }

            // Follow Button
            final Button userFollowButton = (Button) rootView.findViewById(R.id.public_profile_follow_me_button);
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
        }
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

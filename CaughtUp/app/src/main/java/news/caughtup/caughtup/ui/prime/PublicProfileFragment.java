package news.caughtup.caughtup.ui.prime;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import news.caughtup.caughtup.R;
import news.caughtup.caughtup.entities.ResponseObject;
import news.caughtup.caughtup.entities.User;
import news.caughtup.caughtup.util.ImageManager;
import news.caughtup.caughtup.ws.remote.Callback;
import news.caughtup.caughtup.ws.remote.RestProxy;

public class PublicProfileFragment extends Fragment {

    private View rootView;
    private Context context;
    private User user;
    private GridLayout followersGrid;
    private Map<String, View> followersMap;
    private RestProxy proxy;
    private Drawable pressedButton;
    private Drawable normalButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_public_profile, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        context = activity.getApplicationContext();
        Bundle args = getArguments();
        followersMap = new HashMap<>();

        pressedButton = getResources().getDrawable(R.drawable.custom_button_selected, null);
        normalButton = getResources().getDrawable(R.drawable.custom_button_normal, null);

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

    private void setFollowersMessage(List<User> followers) {
        String followersMsg = String.format("%s has %d followers",
                user.getName(),
                followers != null ? followers.size() : 0);
        TextView followersTextView = (TextView) rootView.findViewById(R.id.public_profile_followers_text_view);
        followersTextView.setText(followersMsg);
    }

    private void setUpUser() {
        if(user != null) {
            // UserName
            TextView userNameView = (TextView) rootView.findViewById(R.id.public_profile_username_text_view);
            userNameView.setText(user.getName());

            // Gender
            TextView genderView = (TextView) rootView.findViewById(R.id.public_profile_gender_text_view);
            String gender = user.getGender();
            if(gender != null && !gender.isEmpty()) {
                genderView.setText(gender);
            }

            // Age
            TextView ageView = (TextView) rootView.findViewById(R.id.public_profile_age_text_view);
            int age = user.getAge();
            if(age > 1) {
                ageView.setText(String.format("%d", age));
            }

            // Full Name
            TextView fullNameView = (TextView) rootView.findViewById(R.id.public_profile_full_name_text_view);
            String fullName = user.getFullName();
            if(fullName != null && !fullName.isEmpty()) {
                fullNameView.setText(user.getFullName());
            }

            // Location
            TextView locationView = (TextView) rootView.findViewById(R.id.public_profile_location_text_view);
            String location = user.getLocation();
            if(location != null && !location.isEmpty()) {
                locationView.setText(location);
            }

            // About Me
            TextView aboutMeView = (TextView) rootView.findViewById(R.id.public_profile_about_me_text_view);
            String aboutMe = user.getAboutMe();
            if(aboutMe != null && !aboutMe.isEmpty()) {
                aboutMeView.setText(aboutMe);
            }

            // Profile Picture
            ImageView profilePicView = (ImageView) rootView.findViewById(R.id.public_profile_user_picture_image_view);
            new ImageManager(profilePicView, getActivity(), user.getProfileImageURL());

            // Followers
            List<User> followers = user.getFollowers();
            followersGrid = (GridLayout) rootView.findViewById(R.id.public_profile_followers_grid);
            if (followers != null) {
                for (User follower : followers) {
                    addFollower(follower);
                }
            }
            setFollowersMessage(followers);

            // Follow Button
            final TextView userFollowButton = (TextView) rootView.findViewById(R.id.public_profile_follow_me_button);
            final User currentUser = HomeActivity.getCurrentUser();
            if(currentUser.isFollowing(user.getResourceId())) {
                userFollowButton.setText(getResources().getString(R.string.unfollow_button_text));
                userFollowButton.setBackground(pressedButton);
            }
            userFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String label = userFollowButton.getText().toString();
                    Callback callback = getFollowCallback(userFollowButton);
                    if (label.equals(getResources().getString(R.string.follow_me_button_text))) {
                        proxy.postCall(String.format("/follow?user_id=%d&resource_id=%d", currentUser.getUserId(),
                                user.getResourceId()), "", callback);
                    } else {
                        proxy.deleteCall(String.format("/follow?user_id=%d&resource_id=%d", currentUser.getUserId(),
                                user.getResourceId()), "", callback);
                    }
                }
            });
        }
    }

    private void addFollower(final User follower) {
        LinearLayout followerLayout = new LinearLayout(context);
        followerLayout.setOrientation(LinearLayout.VERTICAL);

        // Add picture to layout and load image asynchronously.
        ImageView profilePic = new ImageView(context);
        new ImageManager(profilePic, getActivity(), follower.getProfileImageURL());

        followerLayout.addView(profilePic,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Add username to layout
        TextView userName = new TextView(context);
        userName.setTextColor(Color.BLACK);
        userName.setText(follower.getName());
        followerLayout.addView(userName,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Add onTouchListener that launches public profile of follower
        followerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PublicProfileFragment fragment = new PublicProfileFragment();
                Bundle args = new Bundle();
                args.putString("username", follower.getName());
                fragment.setArguments(args);
                HomeActivity.executeTransaction(fragment, follower.getName(), follower.getName());
                return true;
            }
        });

        // add layout to grid
        followersGrid.addView(followerLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        followersMap.put(follower.getName(), followerLayout);
    }

    private Callback getFollowCallback(final TextView userFollowButton) {
        return new Callback() {
            @Override
            public void process(ResponseObject responseObject) {
                if (responseObject.getResponseCode() == 200) {
                    User currentUser = HomeActivity.getCurrentUser();
                    String label = userFollowButton.getText().toString();
                    if (label.equals(getResources().getString(R.string.follow_me_button_text))) {
                        addFollower(currentUser);
                        user.addFollower(currentUser);
                        setFollowersMessage(user.getFollowers());
                        userFollowButton.setText(getResources().getString(R.string.unfollow_button_text));
                        userFollowButton.setBackground(pressedButton);
                        currentUser.follow(user);
                        Toast.makeText(context,
                                String.format("Now following %s!", user.getName()),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        user.removeFollower(currentUser);
                        View followerView = followersMap.get(currentUser.getName());
                        followersGrid.removeView(followerView);
                        followersMap.remove(currentUser.getName());
                        setFollowersMessage(user.getFollowers());
                        userFollowButton.setText(getResources().getString(R.string.follow_me_button_text));
                        userFollowButton.setBackground(normalButton);
                        currentUser.unfollow(user.getResourceId());
                        Toast.makeText(context,
                                String.format("Stopped following %s.", user.getName()),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getResources().getString(R.string.follow_server_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}

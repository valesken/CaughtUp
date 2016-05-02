package news.caughtup.caughtup.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import news.caughtup.caughtup.R;

/**
 * Created by jeff on 4/3/16.
 *
 * Temporary model used for prototyping.
 */
public class User extends Resource implements ICaughtUpItem {

    //region Private instance variables
    private int profileImageId = R.mipmap.profile_pic_1;
    private int userId;
    private String fullName;
    private int age;
    private char gender;
    private String location;
    private String aboutMe;
    private String email;
    private List<User> followers;
    private Map<Integer, Resource> followingMap;
    //endregion

    //region Constructors
    public User(String userName) {
        super(userName);
        followers = new LinkedList<>();
        followingMap = new HashMap<>();
    }

    public User(JSONObject jsonObject) throws JSONException {
        // Username, UserId, and ResourceId must always be provided
        super(jsonObject.getString("username"));
        setResourceId(jsonObject.getInt("resourceId"));
        userId = jsonObject.getInt("userId");

        followers = new LinkedList<>();
        followingMap = new HashMap<>();

        // Full Name
        try {
            fullName = jsonObject.getString("fullName");
        } catch (JSONException ignored) {
            fullName = "";
        }

        // Gender
        try {
            gender = jsonObject.getString("gender").charAt(0);
        } catch (JSONException ignored) {
            gender = 'x';
        }

        // Location
        try {
            location = jsonObject.getString("location");
        } catch (JSONException ignored) {
            location = "";
        }

        // Age
        try {
            age = jsonObject.getInt("age");
        } catch (JSONException ignored) {
            age = -1;
        }

        // About Me
        try {
            aboutMe = jsonObject.getString("aboutMe");
        } catch (JSONException ignored) {
            aboutMe = "";
        }

        // Email
        try {
            email = jsonObject.getString("email");
        } catch (JSONException ignored) {
            email = "";
        }
    }
    //endregion

    //region Setters
    public void setProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFollower(User user) {
        followers.add(user);
    }

    public void clearFollowing() {
        followingMap.clear();
    }

    public void follow(Resource resource) {
        followingMap.put(resource.getResourceId(), resource);
    }

    public void unfollow(int resourceId) {
        followingMap.remove(resourceId);
    }
    //endregion

    //region Getters
    public int getProfileImageId() {
        return profileImageId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public char getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getEmail() {
        return email;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<Resource> getFollowing() {
        return new LinkedList<>(followingMap.values());
    }

    public boolean isFollowing(int resourceId) {
        return followingMap.containsKey(resourceId);
    }
    //endregion
}

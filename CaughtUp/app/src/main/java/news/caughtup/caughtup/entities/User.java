package news.caughtup.caughtup.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeff on 4/3/16.
 *
 * Temporary model used for prototyping.
 */
public class User {

    // Private instance variables
    private int profileImageId;
    private String userName;
    private String fullName;
    private int age;
    private char gender;
    private String location;
    private String aboutMe;
    private List<User> followers;

    // Constructor
    public User(String userName) {
        this.userName = userName;
        followers = new LinkedList<>();
    }

    // Setters
    public void setProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void addFollower(User user) {
        followers.add(user);
    }

    // Getters
    public int getProfileImageId() {
        return profileImageId;
    }

    public String getUserName() {
        return userName;
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

    public List<User> getFollowers() {
        return followers;
    }
}

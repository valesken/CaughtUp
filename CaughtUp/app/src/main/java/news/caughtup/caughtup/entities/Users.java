package news.caughtup.caughtup.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Holds a list of users.
 */
public class Users implements Iterable<User> {
    private static Users instance;
    private Map<String, User> userMap = new HashMap<>();

    private Users() { }

    /**
     * Returns the singleton of Users.
     * @return
     */
    public static Users getInstance() {
        if(instance == null) {
            instance = new Users();
        }
        return instance;
    }

    /**
     * Adds a new user to the userList.
     * @param user
     */
    public void addToUserList(User user) {
        userMap.put(user.getName(), user);
    }

    /**
     * Returns an iterator for iterating over users.
     * @return
     */
    @Override
    public Iterator<User> iterator() {
        return getUsers().iterator();
    }

    /*
    Getters and Setters.
     */
    public User getUser(String userName) {
        return userMap.get(userName);
    }

    public List<User> getUsers() {
        return new LinkedList<>(userMap.values());
    }
}

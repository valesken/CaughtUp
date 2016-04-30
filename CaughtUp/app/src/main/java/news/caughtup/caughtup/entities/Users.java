package news.caughtup.caughtup.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Users implements Iterable<User> {

    private static Users instance;
    private Map<String, User> userMap = new HashMap<>();

    private Users() { }

    public static Users getInstance() {
        if(instance == null) {
            instance = new Users();
        }
        return instance;
    }

    public void addToUserList(User user) {
        userMap.put(user.getName(), user);
    }

    public User getUser(String userName) {
        return userMap.get(userName);
    }

    public List<User> getUsers() {
        return new LinkedList<>(userMap.values());
    }

    @Override
    public Iterator<User> iterator() {
        return getUsers().iterator();
    }
}

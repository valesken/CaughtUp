package news.caughtup.caughtup.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserList {
    private static Map<String, User> userMap = new HashMap<>();

    public static void addToUserList(User user) {
        userMap.put(user.getUserName(), user);
    }

    public static User getUser(String userName) {
        return userMap.get(userName);
    }

    public static List<User> getUsers() {
        return new LinkedList<>(userMap.values());
    }
}

package repository;

import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserRepository {

    private List<User> users = new ArrayList<>();

    public User findById(int id) {
        for (User user : users) {
            if (user.getUserId() == id) {
                return user;
            }
        }
        return null;
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public void save(User user) {
        users.add(user);
    }
}
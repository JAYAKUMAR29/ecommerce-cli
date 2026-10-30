package repository;

import com.ecommerce.models.User;
import java.util.ArrayList;
import java.util.List;

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
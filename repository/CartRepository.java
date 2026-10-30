package repository;

import com.ecommerce.models.CartItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRepository {

    private Map<Integer, List<CartItem>> carts = new HashMap<>();

    public List<CartItem> findByUserId(int userId) {
        return new ArrayList<>(
            carts.getOrDefault(userId, new ArrayList<>())
        );
    }

    public void save(int userId, CartItem item) {
        carts.putIfAbsent(userId, new ArrayList<>());
        carts.get(userId).add(item);
    }

    public boolean remove(int userId, int productId) {

        List<CartItem> items = carts.get(userId);

        if (items == null) {
            return false;
        }

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getProduct().getProductId() == productId) {
                items.remove(i);
                return true;
            }
        }

        return false;
    }

    public void clear(int userId) {
        carts.remove(userId);
    }
}
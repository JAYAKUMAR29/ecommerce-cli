package repository;

import java.util.ArrayList;
import java.util.List;
import model.Order;

public class OrderRepository {

    private List<Order> orders = new ArrayList<>();

    public void save(Order order) {
        orders.add(order);
    }

    public Order findById(int id) {
        for (Order order : orders) {
            if (order.getOrderId() == id) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> userOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getUserId() == userId) {
                userOrders.add(order);
            }
        }

        return userOrders;
    }

    public boolean update(Order updatedOrder) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId() == updatedOrder.getOrderId()) {
                orders.set(i, updatedOrder);
                return true;
            }
        }

        return false;
    }
}
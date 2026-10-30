package repository;

import com.ecommerce.models.Payment;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private List<Payment> payments = new ArrayList<>();

    public void save(Payment payment) {
        payments.add(payment);
    }

    public Payment findById(int id) {
        for (Payment payment : payments) {
            if (payment.getPaymentId() == id) {
                return payment;
            }
        }
        return null;
    }

    public Payment findByOrderId(int orderId) {
        for (Payment payment : payments) {
            if (payment.getOrderId() == orderId) {
                return payment;
            }
        }
        return null;
    }

    public boolean update(Payment updatedPayment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId() == updatedPayment.getPaymentId()) {
                payments.set(i, updatedPayment);
                return true;
            }
        }

        return false;
    }
}
package repository;

import com.ecommerce.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private List<Product> products = new ArrayList<>();

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Product findById(int id) {

        for (Product product : products) {

            if (product.getProductId() == id) {
                return product;
            }
        }

        return null;
    }

    public void save(Product product) {
        products.add(product);
    }

    public boolean update(Product updatedProduct) {

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getProductId()
                    == updatedProduct.getProductId()) {

                products.set(i, updatedProduct);
                return true;
            }
        }

        return false;
    }

    public boolean deleteById(int id) {

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getProductId() == id) {
                products.remove(i);
                return true;
            }
        }

        return false;
    }
}
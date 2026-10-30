# E-Commerce CLI — API Contract

> **Purpose:** This document is the shared agreement between team members about class names, method signatures, responsibilities, and how project layers communicate.
>
> **Rule:** Before changing a public class or method signature, discuss it with the team and update this document so callers and implementers stay in sync.

## 1. Project Structure

```text
ecommerce-cli/
├── admin/
├── cli/
├── model/
├── repository/
├── service/
├── docs/
│   └── class-diagram.png
├── Main.java
├── API-CONTRACT.md
└── README.md
```

Use package names that match the actual project structure. If the team uses Java packages, agree on package declarations before implementation.

## 2. Team Ownership

| Member | Main Responsibility | Expected Files |
|---|---|---|
| Member 1 | CLI and user interaction | `cli/` |
| Member 2 | Data models and enums | `model/` |
| Member 3 | Data storage and repository layer | `repository/` |
| Member 4 | Business logic | `service/` |
| Member 5 (Integrator) | Admin flow, integration, reviews, testing | `admin/`, `Main.java`, contract updates |

**Important:** Ownership helps divide work, but it does not restrict what a Git branch can contain. Each member should stage only the files they intend to commit and coordinate changes to shared files.

## 3. Model Contract

### `Product`

**Fields:**
- `int productId`
- `String name`
- `String category`
- `double price`
- `int stock`

**Methods:**
- `int getProductId()`
- `String getName()`
- `String getCategory()`
- `double getPrice()`
- `int getStock()`
- Setters or update methods as agreed by the model owner.

### `User`

**Fields:**
- `int userId`
- `String name`
- `String email`
- `String password`
- `UserRole role`

### `CartItem`

**Fields:**
- `int cartItemId`
- `Product product`
- `int quantity`

**Method:**
- `double getSubtotal()` — returns product price multiplied by quantity.

### `Order`

**Fields:**
- `int orderId`
- `int userId`
- `List<OrderItem> items`
- `double totalAmount`
- `OrderStatus status`

**Method:**
- `double calculateTotal()` — calculates the sum of the order-item subtotals.

### `OrderItem`

**Fields:**
- `Product product`
- `int quantity`
- `double unitPrice`

**Method:**
- `double getSubtotal()` — returns `unitPrice * quantity`.

### `Payment`

**Fields:**
- `int paymentId`
- `int orderId`
- `double amount`
- `PaymentMethod method`
- `PaymentStatus status`
- `String transactionId`

### Enums

```java
public enum UserRole {
    CUSTOMER, ADMIN
}
```

```java
public enum OrderStatus {
    PLACED, CANCELLED, DELIVERED
}
```

```java
public enum PaymentMethod {
    UPI,
    CREDIT_CARD,
    DEBIT_CARD,
    NET_BANKING,
    WALLET,
    CASH_ON_DELIVERY
}
```

```java
public enum PaymentStatus {
    PENDING, SUCCESS, FAILED, REFUNDED
}
```

## 4. Repository Contract

Repositories manage storage and retrieval. They should not contain CLI prompts or print menus.

### `ProductRepository`

```java
List<Product> findAll();

Product findById(int id);

void save(Product product);

boolean update(Product product);

boolean deleteById(int id);
```

### `UserRepository`

```java
User findById(int id);

User findByEmail(String email);

void save(User user);
```

### `CartRepository`

```java
List<CartItem> findByUserId(int userId);

void save(int userId, CartItem item);

boolean remove(int userId, int productId);

void clear(int userId);
```

### `OrderRepository`

```java
void save(Order order);

Order findById(int id);

List<Order> findByUserId(int userId);

boolean update(Order order);
```

### `PaymentRepository`

```java
void save(Payment payment);

Payment findById(int id);

Payment findByOrderId(int orderId);

boolean update(Payment payment);
```

**Initial implementation:** If the team is building an in-memory CLI first, repositories may use `ArrayList` or `Map`. Keep their method signatures stable so the storage implementation can change later.

## 5. Service Contract

Services contain business rules and call repositories. They should not read keyboard input or print menus.

### `ProductService`

```java
List<Product> getAllProducts();

Product getProductById(int id);

List<Product> searchProducts(String keyword);

void addProduct(Product product);

void updateProduct(Product product);

boolean deleteProduct(int id);
```

### `CartService`

```java
void addToCart(int userId, int productId, int quantity);

List<CartItem> viewCart(int userId);

void removeFromCart(int userId, int productId);

double getCartTotal(int userId);

void clearCart(int userId);
```

### `OrderService`

```java
Order checkout(int userId);

List<Order> getOrderHistory(int userId);

boolean cancelOrder(int orderId, int userId);
```

### `PaymentService`

```java
Payment processPayment(int orderId, PaymentMethod method);

PaymentStatus getPaymentStatus(int paymentId);

boolean refundPayment(int paymentId);
```

## 6. CLI Contract

CLI classes are responsible for displaying menus, collecting input, validating basic input format, and calling services.

### `Main`

```java
public static void main(String[] args);
```

### `MainMenu`

```java
void showMenu();
```

### `ProductCLI`

```java
void showProducts();

void searchProduct();
```

### `CartCLI`

```java
void addProductToCart();

void viewCart();

void removeCartItem();
```

### `CheckoutCLI`

```java
void checkout();
```

### `AdminMenu`

```java
void showAdminMenu();

void addProduct();

void updateProduct();

void deleteProduct();

void viewSalesReport();
```

The method names above are the agreed public interface. Internal helper methods may be added as needed.

## 7. Who Calls Whom?

```text
Main
  └── MainMenu
      ├── ProductCLI ──> ProductService ──> ProductRepository
      ├── CartCLI ─────> CartService ─────> CartRepository
      │                    └──────────────> ProductRepository
      ├── CheckoutCLI ─> OrderService ────> OrderRepository
      │                    ├──────────────> CartService
      │                    └──────────────> ProductRepository
      │
      │                 └─> PaymentService ─> PaymentRepository
      │                                      └─> OrderRepository
      │
      └── AdminMenu ───> ProductService
```

`model/` contains the data objects used by these layers. CLI classes must not directly modify repository data; they should call a service.

## 8. Core Workflows

### Browse and Search Products

1. `ProductCLI` calls `ProductService.getAllProducts()` or `searchProducts(keyword)`.
2. `ProductService` retrieves data through `ProductRepository`.
3. CLI displays the returned products.

### Add to Cart

1. `CartCLI` collects product ID and quantity.
2. It calls `CartService.addToCart(userId, productId, quantity)`.
3. `CartService` verifies that the product exists, the quantity is positive, and enough stock is available before saving the cart item.

### Checkout and Payment

1. `CheckoutCLI` calls `OrderService.checkout(userId)`.
2. `OrderService` checks that the cart is not empty and verifies stock before creating an order.
3. `CheckoutCLI` asks for a payment method and calls `PaymentService.processPayment(orderId, method)`.
4. The CLI displays the final order and payment status.

For the CLI prototype, payment processing must be **simulated**. Do not collect or store real card numbers, CVVs, passwords, or UPI PINs.

### Admin Product Management

1. `AdminMenu` collects product details.
2. It calls `ProductService` to add, update, delete, or list products.
3. The service applies validation and uses `ProductRepository` for storage.

## 9. Shared Rules to Agree on Before Coding

The team should confirm these details together:

- **IDs:** Decide how IDs are generated and whether they start at `1`.
- **Missing data:** Decide whether a lookup returns `null` or throws an exception. Use the same rule across the project.
- **Validation:** Product price must not be negative. Stock and quantity must not be negative. Cart quantity must be greater than zero.
- **Stock:** Do not allow checkout when requested quantities exceed available stock.
- **Cart behavior:** Decide whether adding the same product again increases the existing cart quantity.
- **Checkout:** Decide when stock is reduced and what happens if payment fails.
- **Money:** Use `double` only for a simple learning prototype. For production-style money calculations, prefer `BigDecimal`.
- **Authentication:** Do not store plain-text passwords in a real application. For a classroom prototype, agree on the intended scope before implementing login.
- **Errors:** Services should communicate failures consistently, for example through agreed exceptions or result/status objects. CLI should present a readable message to the user.

## 10. Integration and Git Workflow

1. Agree on this contract before implementation.
2. Each member works on their assigned feature branch.
3. Commit only the files relevant to the task.
4. Push the branch and open a pull request into `main`.
5. Integrate in dependency order: **models → repositories → services → CLI/admin → testing**.
6. If a signature needs to change, notify the team and update this file in the same pull request.
7. Run the project and test the full flow after merging.

**Example Git commands:**

```bash
git status
git add model/
git commit -m "Add model classes"
git push origin feature/model
```

Replace the folder and branch names with the ones assigned to you.

## 11. Definition of Done

A feature is ready to merge when:

- It compiles with the agreed class and method signatures.
- It does not make another layer responsible for its own job.
- It handles invalid inputs and missing records according to the shared rules.
- It has been tested with at least one normal case and one invalid/error case.
- The pull request explains what changed and identifies any contract changes.

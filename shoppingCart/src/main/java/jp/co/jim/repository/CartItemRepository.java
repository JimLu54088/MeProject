package jp.co.jim.repository;

import jp.co.jim.entity.Cart;
import jp.co.jim.entity.CartItem;
import jp.co.jim.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart = :cart AND ci.product = :product")
    public CartItem isCartItemInCart(@Param("cart") Cart cart, @Param("product") Product product);
}

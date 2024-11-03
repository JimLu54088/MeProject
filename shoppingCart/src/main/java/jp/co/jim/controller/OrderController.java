package jp.co.jim.controller;

import jp.co.jim.entity.Order;
import jp.co.jim.entity.User;
import jp.co.jim.service.CartService;
import jp.co.jim.service.OrderService;
import jp.co.jim.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService){
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    //建立Session，並建立新的訂單。
    @GetMapping("/create_session")
    public ResponseEntity<Order> createCheckoutSession(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        Long userId = user.getId();
        Integer totalPrice = cartService.clearCart(userId);
        Session session = orderService.createCheckoutSession(totalPrice);
        Order order = orderService.createOrder(session.getId(), totalPrice, session.getPaymentStatus(), session.getUrl(), userId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }

    //找尋用戶的全部訂單
    @GetMapping("/find_order")
    public ResponseEntity<List<Order>> findOrderByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        return ResponseEntity.ok(orderService.findOrderByUserId(user.getId()));
    }
}

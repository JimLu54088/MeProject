package jp.co.jim.controller;

import jp.co.jim.entity.Cart;
import jp.co.jim.entity.User;
import jp.co.jim.request.AddItemRequest;
import jp.co.jim.service.CartService;
import jp.co.jim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    //取得購物車的內容
    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        Cart cart = cartService.calcCartTotal(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    //將商品加入購物車
    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJWT(jwt);
        cartService.addToCart(user.getId(), req);
        return new ResponseEntity<>("Item added to cart",HttpStatus.OK);
    }
}

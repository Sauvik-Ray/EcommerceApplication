package com.ecommerce.project.controller;

import com.ecommerce.project.Repository.CartRepository;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.CartItemDTO;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Shopping Cart", description = "Shopping cart management APIs for handling user cart operations")
@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/cart/create")
    public ResponseEntity<String>createOrUpdateCart(@RequestBody List<CartItemDTO> cartItems){
        String response = cartService.createOrUpdateCartWithItems(cartItems);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Add product to cart", description = "Add a product with specified quantity to user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO>addProductToCart(
            @Parameter(description = "Product ID to add to cart") @PathVariable Long productId,
            @Parameter(description = "Quantity of product to add") @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all carts", description = "Retrieve all shopping carts (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Carts retrieved successfully")
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>>getCarts(){
        List<CartDTO>cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @Operation(summary = "Get user's cart", description = "Retrieve current user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update product quantity in cart", description = "Increase or decrease product quantity in cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO>updateCartProduct(
            @Parameter(description = "Product ID to update") @PathVariable Long productId,
            @Parameter(description = "Operation: 'add' to increase, 'delete' to decrease quantity") @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete")?-1:1);
        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @Operation(summary = "Remove product from cart", description = "Completely remove a product from the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Product or cart not found")
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @Parameter(description = "Cart ID") @PathVariable Long cartId,
            @Parameter(description = "Product ID to remove") @PathVariable Long productId){
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}

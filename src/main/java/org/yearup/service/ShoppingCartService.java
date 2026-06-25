package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        ShoppingCart shoppingCart = new ShoppingCart();

        for(CartItem item : cartItems) {
            Product product = productService.getById(item.getProductId());

            ShoppingCartItem cartItem = new ShoppingCartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(item.getQuantity());

            shoppingCart.add(cartItem);
        }
        return shoppingCart;
    }

    // add additional methods here
    public ShoppingCart addToCart(int userId, int productId)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(existing != null)
        {
            existing.setQuantity(existing.getQuantity() + 1);
            shoppingCartRepository.save(existing);
        }
        else
        {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            shoppingCartRepository.save(newItem);
        }

        return getByUserId(userId);
    }

    public void clearCart(int userId)
    {
        shoppingCartRepository.deleteByUserId(userId);
    }

    public ShoppingCart updateCart(int userId, int productId, int quantity)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if(existing != null)
        {
            existing.setQuantity(quantity);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }
}

package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest
{
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp()
    {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void search_shouldReturn_allProducts_whenNoFilters()
    {
        // arrange
        List<Product> allProducts = List.of(
                new Product(1, "Thriller", 19.99, 1, "Classic album", "Vinyl", 10, false, "thriller.jpg"),
                new Product(2, "Abbey Road", 24.99, 1, "Beatles album", "Vinyl", 5, true, "abbey.jpg")
        );
        when(productRepository.findAll()).thenReturn(allProducts);

        // act
        List<Product> result = productService.search(null, null, null, null);

        // assert
        assertEquals(2, result.size(), "Search with no filters should return all products including non-featured ones");
    }

    @Test
    void update_shouldSave_stockValue()
    {
        // arrange
        Product existing = new Product(1, "Thriller", 19.99, 1, "Classic album", "Vinyl", 10, false, "thriller.jpg");
        Product updated = new Product(1, "Thriller", 19.99, 1, "Classic album", "Vinyl", 50, false, "thriller.jpg");

        when(productRepository.findById(1)).thenReturn(java.util.Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        // act
        Product result = productService.update(1, updated);

        // assert
        assertEquals(50, result.getStock(), "Stock should be updated to 50");
    }
}
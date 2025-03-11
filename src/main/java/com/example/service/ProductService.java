package com.example.service;

import org.springframework.stereotype.Service;
import com.example.model.Product;
import com.example.repository.ProductRepository;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

    private final ProductRepository productRepository;

    // Constructor with Dependency Injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 7.3.2.1 Add New Product
    public Product addProduct(Product product) {
        productRepository.addProduct(product);
        return product;
    }

    // 7.3.2.2 Get All Products
    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    // 7.3.2.3 Get a Specific Product
    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }

    // 7.3.2.4 Update a Product
    public Product updateProduct(UUID productId, String newName, double newPrice) {
        Product product = productRepository.getProductById(productId);
        if (product != null) {
            productRepository.updateProduct(productId, newName, newPrice); // ✅ Correct method call
            product.setName(newName);
            product.setPrice(newPrice);
        }
        return product;
    }


    // 7.3.2.5 Apply Discount
    // 7.3.2.5 Apply Discount
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        for (UUID productId : productIds) {
            Product product = productRepository.getProductById(productId);
            if (product != null) {
                double discountedPrice = product.getPrice() * (1 - discount / 100);
                productRepository.updateProduct(productId, product.getName(), discountedPrice); // ✅ Correct method call
            }
        }
    }


    // 7.3.2.6 Delete a Product
    public void deleteProductById(UUID productId) {
        productRepository.deleteProductById(productId);
    }
}

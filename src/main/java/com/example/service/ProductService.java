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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        productRepository.addProduct(product);
        return product;
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        Product product = productRepository.getProductById(productId);
        if (product != null) {
            productRepository.updateProduct(productId, newName, newPrice); // ✅ Correct method call
            product.setName(newName);
            product.setPrice(newPrice);
        }
        return product;
    }


   
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        for (UUID productId : productIds) {
            Product product = productRepository.getProductById(productId);
            if (product != null) {
                double discountedPrice = product.getPrice() * (1 - discount / 100);
                productRepository.updateProduct(productId, product.getName(), discountedPrice); // ✅ Correct method call
            }
        }
    }


    public void deleteProductById(UUID productId) {
        productRepository.deleteProductById(productId);
    }
}

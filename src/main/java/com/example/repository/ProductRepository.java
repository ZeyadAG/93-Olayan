package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    private static final String PRODUCTS_JSON_PATH = "data/products.json"; // JSON file path

    public ProductRepository() {
    }

    @Override
    protected String getDataPath() {
        return PRODUCTS_JSON_PATH;
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class; // Correct type for JSON deserialization
    }


    public Product addProduct(Product product) {
        save(product);
        return product;
    }


    public ArrayList<Product> getProducts() {
        return findAll();
    }


    public Product getProductById(UUID productId) {
        return getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElse(null);
    }


    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = getProducts();
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                overrideData(products); // Save updated list
                return product;
            }
        }
        return null;
    }


    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = getProducts();
        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - discount / 100);
                product.setPrice(newPrice);
            }
        }
        overrideData(products); // Save changes
    }


    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = getProducts();
        products.removeIf(product -> product.getId().equals(productId));
        overrideData(products);
    }
}


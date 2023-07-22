package com.delivery.system.products.application.deliveryproducts.repositories;

import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Page<Product> findByCategoriesIn(Set<Category> categories, Pageable pageable);
}

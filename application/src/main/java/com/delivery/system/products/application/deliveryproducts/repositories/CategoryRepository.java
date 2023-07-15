package com.delivery.system.products.application.deliveryproducts.repositories;

import com.delivery.system.products.application.deliveryproducts.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


}

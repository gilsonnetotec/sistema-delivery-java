package com.delivery.system.products.application.deliveryproducts.repositories;

import com.delivery.system.products.application.deliveryproducts.Factory;
import com.delivery.system.products.application.deliveryproducts.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId = 1L;

    @Test
    public void getAllShouldGeAllObjectsExists(){
       List<Product> result = repository.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void getByIdShouldGetObjectWhenIdExists(){

        Product entity = Factory.createProduct();

        Product savedDomain = repository.save(entity);

        Long id = savedDomain.getId();

        Product result = repository.findById(id).orElse(null);

       Assertions.assertNotNull(result);

       Assertions.assertEquals(savedDomain.getId(), result.getId());

    }

    @Test
    public void createShouldCreatedObject(){

        Product entity = new Product();

        entity.setName("Test Entity");

        Product result = repository.save(entity);

        Assertions.assertNotNull(result.getId());

        Assertions.assertEquals(result.getName(),"Test Entity");

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdisNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
    }


}

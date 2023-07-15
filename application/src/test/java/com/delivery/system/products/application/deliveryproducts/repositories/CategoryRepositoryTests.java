package com.delivery.system.products.application.deliveryproducts.repositories;

import com.delivery.system.products.application.deliveryproducts.Factory;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    private long exintingId = 1L;

    @Test
    public void getAllShouldGeAllObjectsExists(){
       List<Category> result = repository.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void getByIdShouldGetObjectWhenIdExists(){

        Category entity = Factory.createCategory();

        Category savedDomain = repository.save(entity);

        Long id = savedDomain.getId();

        Category result = repository.findById(id).orElse(null);

       Assertions.assertNotNull(result);

       Assertions.assertEquals(savedDomain.getId(), result.getId());

    }

    @Test
    public void createShouldCreatedObject(){

        Category entity = new Category();

        entity.setName("Test Entity");

        Category result = repository.save(entity);

        Assertions.assertNotNull(result.getId());

        Assertions.assertEquals(result.getName(),"Test Entity");

    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(exintingId);

        Optional<Category> result = repository.findById(exintingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdisNull(){
        Category category = Factory.createCategory();
        category.setId(null);

        category = repository.save(category);

        Assertions.assertNotNull(category.getId());
    }


}

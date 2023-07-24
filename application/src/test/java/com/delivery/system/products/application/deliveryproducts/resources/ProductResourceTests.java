package com.delivery.system.products.application.deliveryproducts.resources;

import com.delivery.system.products.application.deliveryproducts.Factory;
import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.entities.Product;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.repositories.ProductRepository;
import com.delivery.system.products.application.deliveryproducts.services.CategoryService;
import com.delivery.system.products.application.deliveryproducts.services.ProductService;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductResourceTests {

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService service;

    @InjectMocks
    private CategoryService categoryService;

    private Product entity;

    private ProductDTO dto;

    private Category category;

    private Set<Category> categories;

    private long existingId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        entity = Factory.createProduct();
        dto = Factory.createdProductDTO();
        category = Factory.createCategory();

        categories = new HashSet<>();
        categories.add(new Category(category.getId(), category.getName()));
        categoryRepository.findById(1L).ifPresent(categories::add);
    }

    @Test
    public void getAllShouldReturnPageWithOneEntity(){

        categoryRepository.findById(1L).ifPresent(categories::add);

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Product> entityList = Arrays.asList(entity);

        Page<Product> page = new PageImpl<>(entityList, pageRequest, 1);

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Mockito.when(repository.findByCategoriesIn(categories,pageRequest)).thenReturn(page);

        Page<ProductDTO> resultPage = service.findAll(pageRequest, "1");

        Assertions.assertEquals(1, resultPage.getTotalElements());// Verifica se a página contém exatamente uma entidade
        Assertions.assertEquals(entity.getId(), resultPage.getContent().get(0).getId());
        Assertions.assertEquals(entity.getName(), resultPage.getContent().get(0).getName());
    }


    @Test
    public void getShouldReturnEntityWhenSearchForOneId() {
        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        ProductDTO result = service.findById(entity.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals("Teste de Produto", result.getName());
    }

    @Test
    public void getShouldNotReturnEntityWhenProduct_NotFound(){
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> service.findById(existingId));

        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdisNull(){
        entity.setId(null);
        dto.setId(null);

        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(categoryRepository.existsById(category.getId())).thenReturn(true);

        ProductDTO result = service.insert(dto);

        Assertions.assertEquals(entity.getId(), result.getId());
        Assertions.assertEquals(entity.getName(), result.getName());

        Mockito.verify(repository).save(entity);

    }

    @Test
    public void updateShouldPersistWithAutoincrementWhenHasId(){
        Mockito.when(repository.getOne(existingId)).thenReturn(entity);
        Mockito.lenient().when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
        Mockito.when(categoryRepository.existsById(category.getId())).thenReturn(true);


        Assertions.assertNotNull(dto.getCategories());
        dto.getCategories().forEach(c -> {
            Assertions.assertNotNull(c.getId());
            Assertions.assertNotNull(c.getName());
        });

        Assertions.assertNotNull(category);

        dto.setName("Novo Produto");

        ProductDTO result = service.update(existingId, dto);

        Assertions.assertEquals("Novo Produto", result.getName());
        Mockito.verify(repository, Mockito.times(1)).getOne(1L);
        Mockito.lenient().when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
    }

    @Test
    public void updateShouldNotPersistWhenProduct_NotFound(){
        Mockito.when(repository.getOne(existingId)).thenThrow(EntityNotFoundException.class);

        dto.setName("Nova Categoria");

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(existingId, dto));

        Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());
    }

}

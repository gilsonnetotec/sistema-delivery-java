package com.delivery.system.products.application.deliveryproducts.resources;

import com.delivery.system.products.application.deliveryproducts.Factory;
import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.services.CategoryService;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoryResourceTests {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    private Category entity;

    private CategoryDTO dto;

    private long existingId = 1L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        entity = Factory.createCategory();
        dto = Factory.createdCategoryDTO();
    }

    @Test
    public void getAllShouldReturnPageWithOneEntity(){
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Category> entityList = Arrays.asList(entity);

        Page<Category> page = new PageImpl<>(entityList, pageRequest, 1);

        Mockito.when(repository.findAll(pageRequest)).thenReturn(page);

        Page<CategoryDTO> resultPage = service.findAll(pageRequest);

        Assertions.assertEquals(1, resultPage.getTotalElements());// Verifica se a página contém exatamente uma entidade
        Assertions.assertEquals(entity.getId(), resultPage.getContent().get(0).getId());
        Assertions.assertEquals(entity.getName(), resultPage.getContent().get(0).getName());
    }


    @Test
    public void getShouldReturnEntityWhenSearchForOneId() {
        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        CategoryDTO result = service.findById(entity.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals("Teste de Categoria", result.getName());
    }

    @Test
    public void getShouldNotReturnEntityWhenCategory_NotFound(){
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, ()-> service.findById(existingId));

        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdisNull(){
        entity.setId(null);
        dto.setId(null);

        Mockito.when(repository.save(entity)).thenReturn(entity);

        CategoryDTO result = service.insert(dto);

        Assertions.assertEquals(entity.getId(), result.getId());
        Assertions.assertEquals(entity.getName(), result.getName());

        Mockito.verify(repository).save(entity);

    }

    @Test
    public void updateShouldPersistWithAutoincrementWhenHasId(){

        Mockito.when(repository.getOne(existingId)).thenReturn(entity);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(entity);

        dto.setName("Nova Categoria");

        CategoryDTO result = service.update(existingId, dto);

        Assertions.assertEquals("Nova Categoria", result.getName());
        Mockito.verify(repository, Mockito.times(1)).getOne(1L);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any());

    }

    @Test
    public void updateShouldNotPersistWhenCategory_NotFound(){
        Mockito.when(repository.getOne(existingId)).thenThrow(EntityNotFoundException.class);

        dto.setName("Nova Categoria");

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(existingId, dto));

        Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());
    }

}

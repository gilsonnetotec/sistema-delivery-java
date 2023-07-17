package com.delivery.system.products.application.deliveryproducts.rules;

import com.delivery.system.products.application.deliveryproducts.Factory;
import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CategoryRulesTest {

    @Mock
    CategoryRepository repository;

    private CategoryDTO dto;

    private CategoryRule rule;

    private CategoryRule.Handler handler;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        rule = Mockito.mock(CategoryRule.class);;
        dto = Factory.createdCategoryDTO();
        handler = Mockito.mock(CategoryRule.Handler.class);
    }

    @Test
    public void checkNomeIsNullShouldReturnExceptionWhenNomeisNull(){
        dto.setName(null);
        Mockito.when(repository.existsByName(dto.getName())).thenThrow(ResourceNotFoundException.class);
        Mockito.doThrow(new ResourceNotFoundException("Nome não pode ser nullo ou vazio")).when(handler).checkNomeIsNull(dto);
        Assertions.assertFalse(rule.biuld());
    }

    @Test
    public void checkNomeExistShouldReturnExceptionWhenNomeNotExist(){
        dto.setName("Nome não exist");
        Mockito.when(repository.existsByName(dto.getName())).thenThrow(ResourceNotFoundException.class);
        Mockito.doThrow(new ResourceNotFoundException("Este nome já existe na tabela")).when(handler).checkNomeExist(dto);
    }



}

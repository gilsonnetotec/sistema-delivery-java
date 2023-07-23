package com.delivery.system.products.application.deliveryproducts.services;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.entities.Product;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.repositories.ProductRepository;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.DatabaseException;
import com.delivery.system.products.application.deliveryproducts.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest pageRequest, String categoryIds){
        Set<Category> categories = convertCategoryIdsToSet(categoryIds);
        Page<Product> page = repository.findByCategoriesIn(categories,pageRequest);
        return page.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product  entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        try{

            Product entity = new Product();

            copyDtoToEntity(dto, entity);

            entity = repository.save(entity);

            return new ProductDTO(entity, entity.getCategories());
        }catch (EntityNotFoundException e){
            if(dto.getName() == null){
                throw new ResourceNotFoundException("Nome não pode ter valor null");
            }
            throw new ResourceNotFoundException("Erro: "+e);
        }
    }

    @Transactional
    public ProductDTO update(final Long id, ProductDTO dto){
        try{
           Product entity = repository.getOne(id);

            copyDtoToEntity(dto,entity);

            return  new ProductDTO(entity, entity.getCategories());

        }catch (Exception e){
            throw new ResourceNotFoundException("Id "+id+" não encontrado");
        }
    }

    private Set<Category> convertCategoryIdsToSet(String categoryIds) {
        Set<Category> categories = new HashSet<>();
        String[] categoryIdArray = categoryIds.split(",");

        for (String categoryId : categoryIdArray) {
            Long id = Long.parseLong(categoryId.trim());
            categoryRepository.findById(id).ifPresent(categories::add);
        }

        return categories;
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity ){

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();

        for(CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }

    @Transactional
    public  void delete(Long id){
        try {
            Boolean result = repository.existsById(id);
            if(!result){
                throw new ResourceNotFoundException("Id "+id+" não encontrado");
            }
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id "+id+" não encontrado");
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação de integridade");
        }
    }

}

package com.delivery.system.products.application.deliveryproducts.services;

import com.delivery.system.products.application.deliveryproducts.dto.CategoryDTO;
import com.delivery.system.products.application.deliveryproducts.entities.Category;
import com.delivery.system.products.application.deliveryproducts.repositories.CategoryRepository;
import com.delivery.system.products.application.deliveryproducts.rules.CategoryRule;
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

import java.util.Optional;

@Service
public class CategoryService{

    @Autowired
    private CategoryRepository repository;

    public Page<CategoryDTO> findAll(PageRequest pageRequest){
        Page<Category>page = repository.findAll(pageRequest);
        return page.map(category -> new CategoryDTO(category));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        try{
            Boolean rule = new CategoryRule(repository).saved(dto).build();

            Category entity = new Category();

            entity.setId(dto.getId());
            entity.setName(dto.getName());

            entity = repository.save(entity);

            return new CategoryDTO(entity);
        }catch (EntityNotFoundException e){
            if(dto.getName() == null){
                throw new ResourceNotFoundException("Nome não pode ter valor null");
            }
            throw new ResourceNotFoundException("Erro: "+e);
        }
    }

    @Transactional
    public CategoryDTO update(final Long id, CategoryDTO dto){
        try {

            Boolean rule = new CategoryRule(repository).updated(dto).build();

            Category entity = repository.getOne(id);

            entity.setName(dto.getName());

            entity = repository.save(entity);

            return new CategoryDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id "+id+" não encontrado");
        }
    }

    @Transactional
    public void  delete(Long id) {
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

package com.delivery.system.products.application.deliveryproducts.resources;

import com.delivery.system.products.application.deliveryproducts.dto.ProductDTO;
import com.delivery.system.products.application.deliveryproducts.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/produtos")
public class ProductResource {

    @Autowired
    private ProductService service;

    @Operation(description = "API para buscar as produtos por paginação e por categoria")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorno OK da Lista de transações"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "categoryId" /*,defaultValue = "1"*/) String categoryId
    ){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDTO> list = service.findAll(pageRequest, categoryId);
        return ResponseEntity.ok().body(list);
    }

    @Operation(description = "API para buscar um produto por id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorno OK da Lista de transações"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }
}

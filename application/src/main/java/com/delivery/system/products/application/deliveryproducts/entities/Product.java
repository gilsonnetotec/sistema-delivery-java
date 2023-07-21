package com.delivery.system.products.application.deliveryproducts.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Código de identificação")
    private Long id;

    @Schema(description = "Nome do produto")
    @Column(length = 200, nullable = true)
    @NotNull(message = "Informar o nome do produto")
    private String name;

    @Schema(description = "Descrição do produto")
    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Schema(description = "Preço do produto")
    @Null //Se o valor estiver nullo quer dizer que o produto não está disponível.
    private Double price;

    @Schema(description = "URL da imagem do produto")
    @Column(name = "imgURL", columnDefinition = "TEXT", nullable = true)
    private String imgUrl;

    @ManyToMany
    @JoinTable(
            name        = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories = new HashSet<>();

    public Product(){

    }

    public Product(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

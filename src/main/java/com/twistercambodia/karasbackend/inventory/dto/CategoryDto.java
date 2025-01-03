package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Category;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDto {
    private String id;
    private String name;
    private List<ProductDto> products;
    private int productCount;

    public CategoryDto() {}

    public CategoryDto(Category category) {
        ModelMapper mapper = new ModelMapper();
        this.id = category.getId();
        this.name = category.getName();
        this.products = category.getProducts().stream()
                .map((p) -> mapper.map(p, ProductDto.class))
                .collect(Collectors.toList());
        this.productCount = category.getProductCount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}

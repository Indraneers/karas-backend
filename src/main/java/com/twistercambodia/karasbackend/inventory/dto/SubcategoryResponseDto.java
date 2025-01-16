package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Subcategory;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubcategoryResponseDto {
    private String id;
    private String name;
    private CategoryDto category;
    private List<ProductRequestDto> products;
    private int productCount;
    private String img;
    private String color;

    public SubcategoryResponseDto() {}

    public SubcategoryResponseDto(Subcategory subcategory) {
        ModelMapper mapper = new ModelMapper();
        this.id = subcategory.getId();
        this.name = subcategory.getName();
        this.category = new CategoryDto(subcategory.getCategory());
        this.products = subcategory.getProducts() != null ?
                subcategory.getProducts()
                        .stream()
                        .map((p) -> mapper.map(p, ProductRequestDto.class))
                        .collect(Collectors.toList())
                :
                new ArrayList<>();
        this.productCount = subcategory.getProductCount();
        this.img = subcategory.getImg();
        this.color = subcategory.getColor();
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

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public List<ProductRequestDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequestDto> products) {
        this.products = products;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

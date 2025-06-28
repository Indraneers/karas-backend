package com.twistercambodia.karasbackend.inventory.dto;

import com.twistercambodia.karasbackend.inventory.entity.Unit;
import org.modelmapper.ModelMapper;

public class UnitResponseDto {
    private String id;
    private String name;
    private long quantity;
    private ProductRequestDto product;
    private String productImg;
    private String subcategory;
    private String subcategoryImg;
    private String category;
    private String img;
    private int price;
    private long toBaseUnit;

    public UnitResponseDto() {}

    public UnitResponseDto(Unit unit) {
        ModelMapper mapper = new ModelMapper();
        this.id = unit.getId();
        this.name = unit.getName();
        this.quantity = unit.getQuantity();
        this.product = mapper.map(unit.getProduct(), ProductRequestDto.class);
        this.productImg = unit.getProduct().getImg();
        this.subcategory = unit.getSubcategoryName();
        this.subcategoryImg = unit.getSubcategoryImg();
        this.category = unit.getCategoryName();
        this.price = unit.getPrice();
        this.toBaseUnit = unit.getToBaseUnit();
        this.img = unit.getImg();
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public ProductRequestDto getProduct() {
        return product;
    }

    public void setProduct(ProductRequestDto product) {
        this.product = product;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSubcategoryImg() {
        return subcategoryImg;
    }

    public void setSubcategoryImg(String subcategoryImg) {
        this.subcategoryImg = subcategoryImg;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getToBaseUnit() {
        return toBaseUnit;
    }

    public void setToBaseUnit(long toBaseUnit) {
        this.toBaseUnit = toBaseUnit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

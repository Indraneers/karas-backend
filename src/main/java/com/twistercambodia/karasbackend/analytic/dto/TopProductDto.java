package com.twistercambodia.karasbackend.analytic.dto;

public class TopProductDto {
    private String productId;
    private String productName;
    private long revenue;
    private long unitsSold;

    public TopProductDto(String productId, String productName, long revenue, long unitsSold) {
        this.productId = productId;
        this.productName = productName;
        this.revenue = revenue;
        this.unitsSold = unitsSold;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(long unitsSold) {
        this.unitsSold = unitsSold;
    }
}

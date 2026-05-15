package com.twistercambodia.karasbackend.analytic.dto;

public class RevenueBreakdownDto {
    private String id;
    private String label;
    private long revenue;
    private long orderCount;

    public RevenueBreakdownDto(String id, String label, long revenue, long orderCount) {
        this.id = id;
        this.label = label;
        this.revenue = revenue;
        this.orderCount = orderCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public long getRevenue() { return revenue; }
    public void setRevenue(long revenue) { this.revenue = revenue; }

    public long getOrderCount() { return orderCount; }
    public void setOrderCount(long orderCount) { this.orderCount = orderCount; }
}

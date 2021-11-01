package com.khachatryan.DISTask;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Long integrationCost;

    public Product(Long id, String name, String description, Long integrationCost){
        this.id = id;
        this.name = name;
        this.description = description;
        this.integrationCost = integrationCost;
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

    public Long getIntegrationCost() {
        return integrationCost;
    }

    public void setIntegrationCost(Long integrationCost) {
        this.integrationCost = integrationCost;
    }
}

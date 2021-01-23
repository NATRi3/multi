package com.epam.logistic.entity;

public enum  Product {
    MILK("Milk",0.6, true){

    },
    BREAD("Bread",0.6, false){

    },
    CLOTHES("Clothes", 0.4, false){

    };
    private String name;
    private double weight;
    private final boolean isPerishable;
    private Product(String name, double weight, boolean isPerishable){
        this.name = name;
        this.weight = weight;
        this.isPerishable = isPerishable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isPerishable() {
        return isPerishable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("name='").append(name).append('\'').append(isPerishable);
        sb.append('}');
        return sb.toString();
    }
}

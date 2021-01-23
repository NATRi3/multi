package com.epam.logistic.entity;

import com.epam.logistic.exception.StorageEmptyException;
import com.epam.logistic.exception.StorageFullException;

public class Terminal {

    public Terminal(){

    }

    public void addProduct(Product product) throws StorageFullException {
        Storage.getINSTANCE().addProduct(product);
    }

    public Product getTypeProduct() throws StorageEmptyException {
        return Storage.getINSTANCE().getTypeProduct();
    }

    public Product getProduct(Product product) throws StorageEmptyException {
        return Storage.getINSTANCE().getProduct(product);
    }
}

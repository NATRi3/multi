package com.epam.logistic.entity;

import com.epam.logistic.exception.EmptyException;
import com.epam.logistic.service.TruckState;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Log4j
public class Truck implements Callable<Boolean> {
    private Product product;
    private int productAmount;
    private double volume;
    private TruckState state;
    private Terminal terminal;
    private boolean isPerishable;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public Truck(Product product, int productAmount, TruckState state, double volume){
        this.product = product;
        this.productAmount = productAmount;
        this.state = state;
        this.volume = volume;
        if(product!=null) {
            this.isPerishable = product.isPerishable();
        } else {
            this.isPerishable = false;
        }
    }
    @Override
    public Boolean call() {
        try {
            state.action(this);
            state.action(this);
            state.action(this);
            state.action(this);
            state.action(this);
            return true;
        } catch (InterruptedException e) {
            log.error(e);
            return false;
        }
    }

    public Product getProduct() throws EmptyException {
        if(product!=null||productAmount>1){
            productAmount--;
            return product;
        } else {
            if (productAmount ==1) {
                isPerishable=false;
                Product product1 = product;
                product = null;
                productAmount=0;
                isPerishable = false;
                return product;
            } else {
                throw new EmptyException();
            }
        }
    }

    public boolean addProduct(Product product) {
        if(this.product == null){
            this.product = product;
            productAmount=1;
            isPerishable =  product.isPerishable();
            return true;
        }
        if(this.product.equals(product)){
            if(volume>=product.getWeight()*(productAmount+1)) {
                productAmount++;
                return true;
            }
        }
        return false;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public TruckState getState() {
        return state;
    }

    public void setState(TruckState state) {
        this.state = state;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public boolean isPerishable() {
        return isPerishable;
    }

    public void setPerishable(boolean perishable) {
        isPerishable = perishable;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getCondition(){
        return condition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Truck{");
        sb.append("product=").append(product);
        sb.append(", productAmount=").append(productAmount);
        sb.append('}');
        return sb.toString();
    }
}

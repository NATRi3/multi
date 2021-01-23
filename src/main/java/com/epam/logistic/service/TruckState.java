package com.epam.logistic.service;

import com.epam.logistic.exception.EmptyException;
import com.epam.logistic.entity.Product;
import com.epam.logistic.entity.Storage;
import com.epam.logistic.entity.Truck;
import com.epam.logistic.exception.StorageEmptyException;
import com.epam.logistic.exception.StorageFullException;
import lombok.extern.log4j.Log4j;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Log4j
public enum TruckState {
    ARRIVE_STATE{
        public void action(Truck truck) throws InterruptedException {
            if(Storage.getINSTANCE().stayQuq(truck)){
                truck.setState(TruckState.LOAD_STATE);
                log.info("Получили терминал!");
                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
            } else {
                truck.setState(TruckState.QUEUE_STATE);
            }
        }
    },
    QUEUE_STATE{
        public void action(Truck truck){
            try {
                log.info("Ждем в очереди!");
                truck.getLock().lock();
                truck.getCondition().await();
                truck.getLock().unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            truck.setState(TruckState.ARRIVE_STATE);
        }
    },
    LOAD_STATE{
        public void action(Truck truck){
            if(truck.getProductAmount()>0) {
                log.info(truck.toString() + " Разгружаемся!");
                try {
                    while (truck.getProductAmount() > 0) {
                        Product product = truck.getProduct();
                        truck.getTerminal().addProduct(product);
                        TimeUnit.SECONDS.sleep(new Random().nextInt(2));
                    }
                } catch (EmptyException | StorageFullException | InterruptedException e) {
                    log.error(e);
                }
            } else {
                log.info(truck.toString() + " Загружаемся!");
                try {
                    Product product =truck.getTerminal().getTypeProduct();
                    while (truck.addProduct(product)) {
                        if(truck.getTerminal().getProduct(product)!=null) {
                            TimeUnit.SECONDS.sleep(new Random().nextInt(2));
                        }
                    }
                } catch (InterruptedException | StorageEmptyException e) {
                    log.error(e);
                }
            }
            truck.setState(TruckState.LEAVE_STATE);
        }
    },
    LEAVE_STATE{
        public void action(Truck truck) throws InterruptedException {
            log.info("Покидаем склад!");
            TimeUnit.SECONDS.sleep(new Random().nextInt(7));
            Storage.getINSTANCE().returnTerminal(truck.getTerminal());
            truck.setTerminal(null);
            truck.setState(null);
        }
    };
    public abstract void action(Truck truck) throws InterruptedException;
}

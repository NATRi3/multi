package com.epam.logistic.entity;

import com.epam.logistic.exception.StorageEmptyException;
import com.epam.logistic.exception.StorageFullException;
import com.epam.logistic.util.TruckComparator;
import lombok.extern.log4j.Log4j;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Log4j
public class Storage {
    private static final int DEFAULT_TERMINAL = 5;
    private static Storage INSTANCE;
    private static final Lock locking = new ReentrantLock();
    private static final Lock terminalLock = new ReentrantLock();
    private static final Lock productLock = new ReentrantLock();
    private static final Lock lockQueue = new ReentrantLock();
    private final Map<Product, Integer> products;
    private final Queue<Terminal> terminals;
    private final List<Terminal> busyTerminal;
    private double currentVolume;
    private final double volume;
    private final Comparator<Truck> truckComparator = new TruckComparator();
    private final Queue<Truck> trucks;
    private final Semaphore semaphore;

    public static Storage getINSTANCE(){
        if(INSTANCE==null){
            locking.lock();
            if(INSTANCE==null) {
                INSTANCE = new Storage();
            }
            locking.unlock();
        }
        return INSTANCE;
    }

    public static void createStorage(int amountTerminal, Map<Product,Integer> products, double currentVolume, double volume){
        locking.lock();
        if(INSTANCE==null) {
            INSTANCE = new Storage(amountTerminal,products,currentVolume,volume);
        }
        locking.unlock();
    }

    private Storage(){
        semaphore = new Semaphore(DEFAULT_TERMINAL);
        trucks = new PriorityBlockingQueue<>(50,truckComparator);
        products = new HashMap<>();
        terminals = new ArrayDeque<>();
        busyTerminal = new LinkedList<>();
        for(int i = 0; i<DEFAULT_TERMINAL;i++) {
            terminals.add(new Terminal());
        }
        volume = 1000;
    }
    private Storage(int amountTerminal, Map<Product,Integer> products, double currentVolume, double volume){
        semaphore = new Semaphore(amountTerminal);
        trucks = new PriorityBlockingQueue<>(50,truckComparator);
        this.products = products;
        terminals = new ArrayDeque<>();
        busyTerminal = new LinkedList<>();
        for(int i = 0; i<amountTerminal;i++) {
            terminals.add(new Terminal());
        }
        this.currentVolume = currentVolume;
        this.volume = volume;
    }

    public boolean stayQuq(Truck truck) throws InterruptedException {

        if(semaphore.tryAcquire()) {
            truck.setTerminal(getINSTANCE().getTerminal());
            return true;
        }
        if(truck.isPerishable()){
            semaphore.acquire();
            truck.setTerminal(getINSTANCE().getTerminal());
            return true;
        }
        lockQueue.lock();
        trucks.offer(truck);
        lockQueue.unlock();
        return false;
    }
    private Terminal getTerminal() {
        terminalLock.lock();
        Terminal terminal = terminals.remove();
        busyTerminal.add(terminal);
        terminalLock.unlock();
        return terminal;
    }

    public void returnTerminal(Terminal terminal) {
        terminalLock.lock();
        terminals.add(terminal);
        busyTerminal.remove(terminal);
        terminalLock.unlock();
        semaphore.release();
        if(!trucks.isEmpty()) {
            lockQueue.lock();
            Truck truck = trucks.poll();
            lockQueue.unlock();
            truck.getLock().lock();
            truck.getCondition().signalAll();
            truck.getLock().unlock();
        }
    }


    public void addProduct(Product product) throws StorageFullException {
        if(getVolume()>=getCurrentVolume()+product.getWeight()) {
            productLock.lock();
            if (products.containsKey(product)) {
                products.replace(product, products.get(product) + 1);
            } else {
                products.put(product, 1);
            }
            setCurrentVolume(getCurrentVolume()+product.getWeight());
            productLock.unlock();
        } else {
            throw new StorageFullException();
        }
    }

    public Product getTypeProduct() throws StorageEmptyException {
        if (!products.isEmpty()) {
            productLock.lock();
            Product product = products.keySet().iterator().next();
            productLock.unlock();
            return product;
        }else {
            throw new StorageEmptyException("EMPTY!");
        }
    }
    public Product getProduct(Product product){
        if(!products.containsKey(product)){
            productLock.lock();
            products.replace(product,products.get(product)-1);
            setCurrentVolume(getCurrentVolume()-product.getWeight());
            productLock.unlock();
            return product;
        } else {
            return null;
        }
    }

    public double getVolume() {
        return volume;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(double currentVolume) {
        this.currentVolume = currentVolume;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Storage{");
        sb.append("products=").append(products);
        sb.append('}');
        return sb.toString();
    }
}

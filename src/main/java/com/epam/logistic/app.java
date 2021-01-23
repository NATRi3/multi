package com.epam.logistic;

import com.epam.logistic.entity.Truck;
import com.epam.logistic.exception.ReaderException;
import com.epam.logistic.parse.DataParse;
import com.epam.logistic.parse.StorageParser;
import com.epam.logistic.parse.TruckParser;
import com.epam.logistic.reader.DataFileReader;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class app {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        ExecutorService executor = Executors.newFixedThreadPool(30);
        DataFileReader dataFileReader = new DataFileReader();
        try {
            List<Truck> truckList;
            String data = dataFileReader.readShopFromFile("src/main/resources/data.txt");
            DataParse dataParse = new DataParse();
            String[] strings = dataParse.dataParse(data);
            System.out.println(strings[0]);
            System.out.println(strings[1]);
            TruckParser truckParser = new TruckParser();
            truckList = truckParser.truckParser(strings[0]);
            StorageParser storageParser = new StorageParser();
            storageParser.storageParserAndCreateInstance(strings[1]);
            for(Truck truck: truckList) {
                executor.submit(truck);
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        /*List<Truck> truckList = new ArrayList<>();
        for(int j=0; j<100;j++) {
            Product product = Product.values()[new Random().nextInt(3)];
            truckList.add(new Truck(product, new Random().nextInt(30)+20,TruckState.ARRIVE_STATE, 50));
        }
        truckList.add(new Truck(null, 0, TruckState.ARRIVE_STATE, 50));
        truckList.add(new Truck(null, 0,TruckState.ARRIVE_STATE,50));
        truckList.add(new Truck(null, 0,TruckState.ARRIVE_STATE, 50));
        truckList.add(new Truck(null, 0,TruckState.ARRIVE_STATE, 50));
        for (Truck truck : truckList) {
            executor.submit(truck);
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}

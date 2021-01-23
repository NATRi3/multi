package com.epam.logistic.parse;

import com.epam.logistic.entity.Product;
import com.epam.logistic.entity.Truck;
import com.epam.logistic.service.TruckState;
import java.util.*;
import java.util.List;

public class TruckParser {
    public List<Truck> truckParser(String data){
        String[] strings = data.split(";");
        List<Truck> truckList = new ArrayList<>();
        for(String s: strings){
            String[] s2 = s.split(", ");
            Truck truck = new Truck(Product.valueOf(s2[0].toUpperCase()),Integer.parseInt(s2[1]),
                    TruckState.valueOf(s2[2].toUpperCase()),Double.parseDouble(s2[3]));
            truckList.add(truck);
        }
        return truckList;
    }
}

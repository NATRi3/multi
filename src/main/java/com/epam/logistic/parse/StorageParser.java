package com.epam.logistic.parse;

import com.epam.logistic.entity.Product;
import com.epam.logistic.entity.Storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageParser {
    public static final String regex = "[;]";
    public static final String regex2 = "], \\[";
    public boolean storageParserAndCreateInstance(String data){
        String[] strings = data.split(regex);
        double volume = 0;
        int amountTerminals = 0;
        double currentVolume = 0;
        Map<Product,Integer> products = new HashMap<>();
        for(String s: strings){
            if(s.contains("Terminals:")){
                amountTerminals = Integer.parseInt(s.substring(s.indexOf(":")+1));
            }
            if(s.contains("Products:")){
                String[] strings1 = s.substring(s.indexOf("[")+1).split(regex2);
                strings1[strings1.length-1] = strings1[strings1.length-1].replace("]","");
                for (String s1: strings1){
                    String[] s2 = s1.split(", ");
                    products.put(Product.valueOf(s2[0].toUpperCase()), Integer.parseInt(s2[1]));
                    currentVolume += Product.valueOf(s2[0].toUpperCase()).getWeight()*Integer.parseInt(s2[1]);
                }
            }
            if(s.contains("Volume:")){
                volume = Double.parseDouble(s.substring(s.indexOf(":")+1));
            }
        }
        Storage.createStorage(amountTerminals, products, currentVolume,volume);
        return true;
    }

}

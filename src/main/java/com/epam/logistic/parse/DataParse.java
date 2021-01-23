package com.epam.logistic.parse;

import com.epam.logistic.entity.Product;
import com.epam.logistic.entity.Storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataParse {
    public String[] dataParse(String data){
        String[] strings = data.split("\\.+\\s{3}");
        strings[0] = strings[0].substring(strings[0].indexOf(":")+1);
        strings[1] = strings[1].substring(strings[1].indexOf(":")+1);
        return strings;
    }
}

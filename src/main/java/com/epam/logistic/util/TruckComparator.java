package com.epam.logistic.util;

import com.epam.logistic.entity.Truck;

import java.util.Comparator;

public class TruckComparator implements Comparator<Truck> {

    @Override
    public int compare(Truck o1, Truck o2) {
        Boolean b1 = o1.isPerishable();
        Boolean b2 = o2.isPerishable();
        return b2.compareTo(b1);
    }
}

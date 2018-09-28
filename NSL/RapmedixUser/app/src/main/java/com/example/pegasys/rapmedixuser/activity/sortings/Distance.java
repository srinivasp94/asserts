package com.example.pegasys.rapmedixuser.activity.sortings;

import com.example.pegasys.rapmedixuser.activity.pojo.specDoclist;

import java.util.Comparator;

/**
 * Created by pegasys on 3/8/2018.
 */

public class Distance implements Comparator<specDoclist> {
    @Override
    public int compare(specDoclist specDoclist, specDoclist t1) {
        return specDoclist.distance.compareTo(t1.distance);
    }
}

package com.example.pegasys.rapmedixuser.activity.sortings;

import com.example.pegasys.rapmedixuser.activity.pojo.specDoclist;

import java.util.Comparator;

/**
 * Created by pegasys on 3/8/2018.
 */

public class Experiance implements Comparator<specDoclist> {
    @Override
    public int compare(specDoclist specDoclist, specDoclist t1) {
        return specDoclist.experience.compareTo(t1.experience);
    }
}

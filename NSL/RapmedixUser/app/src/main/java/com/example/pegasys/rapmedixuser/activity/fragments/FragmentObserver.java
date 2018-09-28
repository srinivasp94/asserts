package com.example.pegasys.rapmedixuser.activity.fragments;

import java.util.Observable;

/**
 * Created by rk on 30-Dec-17.
 */

public class FragmentObserver extends Observable {
    @Override
    public void notifyObservers() {
        setChanged(); // Set the changed flag to true, otherwise observers won't be notified.
        super.notifyObservers();
    }
}
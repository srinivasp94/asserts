package com.example.pegasys.rapmedixuser.activity.pojo;

/**
 * Created by pegasys on 2/28/2018.
 */

public class Searchmodel {
    String mid;
    String Name;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Searchmodel{" +
                "mid='" + mid + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}

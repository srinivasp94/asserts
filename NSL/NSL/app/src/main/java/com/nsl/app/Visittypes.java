package com.nsl.app;

/**
 * Created by admin on 12/28/2016.
 */

public class Visittypes {

    private String id;
    private String name;

    public Visittypes(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Country){
            Visittypes f = (Visittypes ) obj;
            if(f.getName().equals(name) && f.getId()==id )
                return true;
        }

        return false;
    }
}

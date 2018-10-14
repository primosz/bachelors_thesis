package com.majchrowski.piotr.inz;

public class Category {

    public Category(int id, String name){
        this.id = id;
        this.name = name;

    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    int id;
    String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

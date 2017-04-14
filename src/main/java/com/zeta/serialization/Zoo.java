package com.zeta.serialization;

import java.util.ArrayList;
import java.util.List;

public class Zoo {

    private String city, name;

    private List<Animal> animals;

    Zoo(String city, String name) {
        this.city = city;
        this.name = name;
        this.animals = new ArrayList<Animal>();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    void add(Animal al) {
        this.animals.add(al);
    }
}

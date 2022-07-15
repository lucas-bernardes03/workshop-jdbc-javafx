package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Star;

public class StarService {
    public List<Star> findAll(){
        List<Star> list = new ArrayList<>();
        list.add(new Star(1, "Sun", "G", 1.0));
        list.add(new Star(2, "Betelgeuse", "M", 14.0));
        list.add(new Star(3, "Saiph", "B", 15.5));
        list.add(new Star(4, "Alpha Centauri", "G", 1.1));
        list.add(new Star(5, "Vega", "A", 2.1));
        return list;
    }
}

package model.dao;

import java.util.List;

import model.entities.Star;

public interface StarDao {
    void insert(Star star);
    void update(Star star);
    void deleteById(Integer id);
    Star findById(Integer id);
    List<Star> findAll();
}

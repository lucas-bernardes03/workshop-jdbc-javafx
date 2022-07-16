package model.dao;

import java.util.List;

import model.entities.Star;
import model.entities.Planet;

public interface PlanetDao {
    void insert(Planet planet);
    void update(Planet planet);
    void deleteById(Integer id);
    Planet findById(Integer id);
    List<Planet> findAll();
    List<Planet> findByStar(Star star);
}

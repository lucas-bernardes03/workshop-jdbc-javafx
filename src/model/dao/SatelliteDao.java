package model.dao;

import java.util.List;

import model.entities.Planet;
import model.entities.Satellite;

public interface SatelliteDao {
    void insert(Satellite satellite);
    void update(Satellite satellite);
    void deleteById(Integer id);
    Satellite findById(Integer id);
    List<Satellite> findAll();
    List<Satellite> findByPlanet(Planet planet);
}

package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PlanetDao;
import model.entities.Planet;

public class PlanetService {
    private PlanetDao dao = DaoFactory.createPlanetDao();

    public List<Planet> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Planet planet){
        if(planet.getId() == null) dao.insert(planet);    
        else dao.update(planet);
    }

    public void remove(Planet planet){
        dao.deleteById(planet.getId());
    }
}

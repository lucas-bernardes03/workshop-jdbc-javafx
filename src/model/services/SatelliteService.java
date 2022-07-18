package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SatelliteDao;
import model.entities.Satellite;

public class SatelliteService {

    private SatelliteDao dao = DaoFactory.createSatelliteDao();

    public List<Satellite> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Satellite satellite){
        if(satellite.getId() == null) dao.insert(satellite);    
        else dao.update(satellite);
    }

    public void remove(Satellite satellite){
        dao.deleteById(satellite.getId());
    }
}

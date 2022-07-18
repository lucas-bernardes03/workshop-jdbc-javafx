package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.StarDao;
import model.entities.Star;

public class StarService {

    private StarDao dao = DaoFactory.createStarDao();

    public List<Star> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Star star){
        if(star.getId() == null) dao.insert(star);    
        else dao.update(star);
    }

    public void remove(Star star){
        dao.deleteById(star.getId());
    }
}

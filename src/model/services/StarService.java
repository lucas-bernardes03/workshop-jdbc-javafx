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
}

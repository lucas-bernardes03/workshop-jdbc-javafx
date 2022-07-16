package model.dao;

import model.dao.impl.StarDaoJDBC;
import model.dao.impl.PlanetDaoJDBC;
import model.db.DB;

public class DaoFactory {
    public static PlanetDao createPlanetDao(){
        return new PlanetDaoJDBC(DB.getConnection());
    }

    public static StarDao createStarDao(){
        return new StarDaoJDBC(DB.getConnection());
    }
}

package model.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.entities.Planet;
import model.entities.Satellite;
import model.entities.Star;

public abstract class instImplementations {
    protected abstract Star instStar(ResultSet rs) throws SQLException;

    protected abstract Planet instPlanet(ResultSet rs, Star star) throws SQLException;

    protected Satellite instSatellite(ResultSet rs, Planet planet) throws SQLException{
        Satellite satellite = new Satellite(rs.getInt("Id"), rs.getString("Name"), planet);
        return satellite;
    }
}

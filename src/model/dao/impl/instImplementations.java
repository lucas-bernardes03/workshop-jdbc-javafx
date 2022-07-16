package model.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.entities.Planet;
import model.entities.Satellite;
import model.entities.Star;

public class instImplementations {
    protected Star instStar(ResultSet rs) throws SQLException{
        Star star = new Star(rs.getInt("StarId"), rs.getString("StarName"), rs.getString("StellarClass"), rs.getDouble("Mass"));
        return star;
    }

    protected Planet instPlanet(ResultSet rs, Star star) throws SQLException{
        Planet planet = new Planet(rs.getInt("Id"), rs.getString("Name"), rs.getString("Type"), rs.getDouble("Diameter"), rs.getDouble("Mass"), rs.getDouble("Gravity"), rs.getDouble("OrbitalSpeed"), star);
        return planet;
    }

    protected Satellite instSatellite(ResultSet rs, Planet planet) throws SQLException{
        Satellite satellite = new Satellite(rs.getInt("Id"), rs.getString("Name"), planet);
        return satellite;
    }
}

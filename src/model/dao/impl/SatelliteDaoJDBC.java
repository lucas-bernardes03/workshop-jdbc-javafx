package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.SatelliteDao;
import model.db.DB;
import model.db.DbException;
import model.entities.Planet;
import model.entities.Satellite;
import model.entities.Star;

public class SatelliteDaoJDBC extends instImplementations implements SatelliteDao{

    private Connection con;

    public SatelliteDaoJDBC(Connection con){
        this.con = con;
    }

    @Override
    public void insert(Satellite satellite) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "INSERT INTO satellite "
                + "(Name, PlanetId) "
                + "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, satellite.getName());
            pst.setInt(2, satellite.getPlanet().getId());

            int updatedRows = pst.executeUpdate();

            if(updatedRows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    satellite.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else throw new DbException("Unexpected error. No rows updated.");
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
        }
        
    }

    @Override
    public void update(Satellite satellite) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "UPDATE satellite "
                + "SET Name = ?, PlanetId = ? "
                + "WHERE Id = ?");
            
            pst.setString(1, satellite.getName());
            pst.setInt(2, satellite.getPlanet().getId());
            pst.setInt(3, satellite.getId());

            pst.executeUpdate();

        }
        catch(SQLException e ){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
        }       
        
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement pst = null;
        try{
            pst = con.prepareStatement("DELETE FROM satellite WHERE Id = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
        }
        
    }

    @Override
    public Satellite findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT satellite.*,planet.Name as PlanetName,star.Name as StarName "
            + "FROM satellite "
            + "INNER JOIN planet ON satellite.PlanetId = planet.Id "
            + "INNER JOIN star ON planet.StarId = star.Id "
            + "WHERE satellite.Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Star star = instStar(rs);
                Planet planet = instPlanet(rs,star);
                Satellite satellite = instSatellite(rs, planet);
                return satellite;
            }
            
            return null;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
        
    }

    @Override
    public List<Satellite> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT satellite.*,planet.Name as PlanetName,star.Name as StarName "
            + "FROM satellite "
            + "INNER JOIN planet ON satellite.PlanetId = planet.Id "
            + "INNER JOIN star ON planet.StarId = star.Id "
            + "ORDER BY Name");

            rs = pst.executeQuery();
            
            List<Satellite> list = new ArrayList<>();
            Map<Integer, Star> mapStar = new HashMap<>();
            Map<Integer, Planet> mapPlanet = new HashMap<>();

            while(rs.next()){
                Star star = mapStar.get(rs.getInt("StarId"));
                
                if(star == null){
                    star = instStar(rs);
                    mapStar.put(rs.getInt("StarId"), star);
                } 
                
                Planet planet = mapPlanet.get(rs.getInt("PlanetId"));
                
                if(planet == null){
                    planet = instPlanet(rs, star);
                    mapPlanet.put(rs.getInt("PlanetId"), planet);
                } 

                Satellite satellite = instSatellite(rs, planet);
                list.add(satellite);
            }
            
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Satellite> findByPlanet(Planet planet) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT satellite.*,planet.Name as PlanetName,star.Name as StarName "
            + "FROM satellite "
            + "INNER JOIN planet ON satellite.PlanetId = planet.Id "
            + "INNER JOIN star ON planet.StarId = star.Id "
            + "WHERE PlanetId = ? "
            + "ORDER BY Name");

            pst.setInt(1, planet.getId());
            rs = pst.executeQuery();
            
            List<Satellite> list = new ArrayList<>();
            Map<Integer, Star> mapStar = new HashMap<>();
            Map<Integer, Planet> mapPlanet = new HashMap<>();

            while(rs.next()){
                Star star = mapStar.get(rs.getInt("StarId"));
                
                if(star == null){
                    star = instStar(rs);
                    mapStar.put(rs.getInt("StarId"), star);
                } 
                
                Planet plt = mapPlanet.get(rs.getInt("PlanetId"));
                
                if(plt == null){
                    plt = instPlanet(rs, star);
                    mapPlanet.put(rs.getInt("PlanetId"), plt);
                } 

                Satellite satellite = instSatellite(rs, plt);
                list.add(satellite);
            }
            
            return list;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    @Override
    protected Star instStar(ResultSet rs) throws SQLException{
        Star star = new Star(rs.getInt("StarId"), rs.getString("StarName"), rs.getString("StellarClass"), rs.getDouble("Mass"));
        return star;
    }

    @Override
    protected Planet instPlanet(ResultSet rs, Star star) throws SQLException{
        Planet planet = new Planet(rs.getInt("PlanetId"), rs.getString("PlanetName"), rs.getString("Type"), rs.getDouble("Diameter"), rs.getDouble("Mass"), rs.getDouble("Gravity"), rs.getDouble("OrbitalSpeed"), star);
        return planet;
    }

    @Override
    protected Satellite instSatellite(ResultSet rs, Planet planet) throws SQLException{
        Satellite satellite = new Satellite(rs.getInt("Id"), rs.getString("Name"), planet);
        return satellite;
    }
    
}

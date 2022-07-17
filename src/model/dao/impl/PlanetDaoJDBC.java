package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.PlanetDao;
import model.db.DB;
import model.db.DbException;
import model.entities.Star;
import model.entities.Planet;

public class PlanetDaoJDBC extends instImplementations implements PlanetDao {

    private Connection con;

    public PlanetDaoJDBC(Connection con){
        this.con = con;
    }

    @Override
    public void insert(Planet planet) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "INSERT INTO planet "
                + "(Name, Type, Diameter, Mass, Gravity, OrbitalSpeed, StarId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, planet.getName());
            pst.setString(2, planet.getType());
            pst.setDouble(3, planet.getDiameter());
            pst.setDouble(4, planet.getMass());
            pst.setDouble(5, planet.getGravity());
            pst.setDouble(6, planet.getOrbitalSpeed());
            pst.setInt(7, planet.getStar().getId());

            int updatedRows = pst.executeUpdate();

            if(updatedRows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    planet.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else throw new DbException("Unexpected error. No rows updated.");
        }
        catch(SQLException e ){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(pst);
        }
    }

    @Override
    public void update(Planet planet) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "UPDATE planet "
                + "SET Name = ?, Type = ?, Diameter = ?, Mass = ?, Gravity = ?, OrbitalSpeed = ?, StarId = ? "
                + "WHERE Id = ?");
            
            pst.setString(1, planet.getName());
            pst.setString(2, planet.getType());
            pst.setDouble(3, planet.getDiameter());
            pst.setDouble(4, planet.getMass());
            pst.setDouble(5, planet.getGravity());
            pst.setDouble(6, planet.getOrbitalSpeed());
            pst.setInt(7, planet.getStar().getId());
            pst.setInt(6, planet.getId());

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
            pst = con.prepareStatement("DELETE FROM planet WHERE Id = ?");
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
    public Planet findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT planet.*,star.Name as StarName "
            + "FROM planet INNER JOIN star "
            + "ON planet.StarId = star.Id "
            + "WHERE planet.Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Star star = instStar(rs);
                Planet planet = instPlanet(rs, star);
                return planet;
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
    public List<Planet> findByStar(Star star){
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT planet.*,star.Name as StarName "
            + "FROM planet INNER JOIN star "
            + "ON planet.StarId = star.Id "
            + "WHERE StarId = ? "
            + "ORDER BY Name");

            pst.setInt(1, star.getId());
            rs = pst.executeQuery();
            
            List<Planet> list = new ArrayList<>();
            Map<Integer, Star> map = new HashMap<>();

            while(rs.next()){
                Star st = map.get(rs.getInt("StarId"));
                
                if(st == null){
                    st = instStar(rs);
                    map.put(rs.getInt("StarId"), st);
                } 

                Planet planet = instPlanet(rs, st);
                list.add(planet);
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
    public List<Planet> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT planet.*,star.Name as StarName "
            + "FROM planet INNER JOIN star "
            + "ON planet.StarId = star.Id "
            + "ORDER BY Name");

            rs = pst.executeQuery();
            
            List<Planet> list = new ArrayList<>();
            Map<Integer, Star> map = new HashMap<>();

            while(rs.next()){
                Star star = map.get(rs.getInt("StarId"));
                
                if(star == null){
                    star = instStar(rs);
                    map.put(rs.getInt("StarId"), star);
                } 

                Planet planet = instPlanet(rs, star);
                list.add(planet);
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
        Planet planet = new Planet(rs.getInt("Id"), rs.getString("Name"), rs.getString("Type"), rs.getDouble("Diameter"), rs.getDouble("Mass"), rs.getDouble("Gravity"), rs.getDouble("OrbitalSpeed"), star);
        return planet;
    }

}

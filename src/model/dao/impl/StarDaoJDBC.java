package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.StarDao;
import model.db.DB;
import model.db.DbException;
import model.entities.Star;

public class StarDaoJDBC implements StarDao {

    private Connection con;

    public StarDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Star star) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement("INSERT INTO star (Name, StellarClass, Mass) VALUES (?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, star.getName());
            pst.setString(2, star.getStellarClass());
            pst.setDouble(3, star.getMass());

            int updatedRows = pst.executeUpdate();

            if(updatedRows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    star.setId(id);
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
    public void update(Star star) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "UPDATE star SET Name = ?, StellarClass = ?, Mass = ?  WHERE Id = ?");
            
            pst.setString(1, star.getName());
            pst.setString(2, star.getStellarClass());
            pst.setDouble(3, star.getMass());
            pst.setInt(4, star.getId());

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
            pst = con.prepareStatement("DELETE FROM star WHERE Id = ?");
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
    public Star findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT * FROM star WHERE Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if(rs.next()) return new Star(rs.getInt("Id"), rs.getString("Name"), rs.getString("StellarClass"), rs.getDouble("Mass"));
            
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
    public List<Star> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement("SELECT * FROM star ORDER BY Name");
            rs = pst.executeQuery();
        
            List<Star> list = new ArrayList<>();
            while(rs.next()){
                list.add(new Star(rs.getInt("Id"), rs.getString("Name"), rs.getString("StellarClass"), rs.getDouble("Mass")));
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

}

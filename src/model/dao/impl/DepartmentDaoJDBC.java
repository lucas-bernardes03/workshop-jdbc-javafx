package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.DepartmentDao;
import model.db.DB;
import model.db.DbException;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection con;

    public DepartmentDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Department dp) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement("INSERT INTO department (Name) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, dp.getName());

            int updatedRows = pst.executeUpdate();

            if(updatedRows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    dp.setId(id);
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
    public void update(Department dp) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "UPDATE department SET Name = ? WHERE Id = ?");
            
            pst.setString(1, dp.getName());
            pst.setInt(2, dp.getId());

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
            pst = con.prepareStatement("DELETE FROM department WHERE Id = ?");
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
    public Department findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT * FROM department WHERE Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if(rs.next()) return new Department(rs.getInt("Id"), rs.getString("Name"));
            
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
    public List<Department> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement("SELECT * FROM department ORDER BY Name");
            rs = pst.executeQuery();
        
            List<Department> list = new ArrayList<>();
            while(rs.next()){
                list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
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

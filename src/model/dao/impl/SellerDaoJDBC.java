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

import model.dao.SellerDao;
import model.db.DB;
import model.db.DbException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

    private Connection con;

    public SellerDaoJDBC(Connection con){
        this.con = con;
    }

    @Override
    public void insert(Seller sl) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "INSERT INTO seller "
                + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                + "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, sl.getName());
            pst.setString(2, sl.getEmail());
            pst.setDate(3, new java.sql.Date(sl.getBirthDate().getTime()));
            pst.setDouble(4, sl.getBaseSalary());
            pst.setInt(5, sl.getDepartment().getId());

            int updatedRows = pst.executeUpdate();

            if(updatedRows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    sl.setId(id);
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
    public void update(Seller sl) {
        PreparedStatement pst = null;

        try{
            pst = con.prepareStatement(
                "UPDATE seller "
                + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                + "WHERE Id = ?");
            
            pst.setString(1, sl.getName());
            pst.setString(2, sl.getEmail());
            pst.setDate(3, new java.sql.Date(sl.getBirthDate().getTime()));
            pst.setDouble(4, sl.getBaseSalary());
            pst.setInt(5, sl.getDepartment().getId());
            pst.setInt(6, sl.getId());

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
            pst = con.prepareStatement("DELETE FROM seller WHERE Id = ?");
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
    public Seller findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT seller.*,department.Name as DepName "
            + "FROM seller INNER JOIN department "
            + "ON seller.DepartmentId = department.Id "
            + "WHERE seller.Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Department dp = instDepartment(rs);
                Seller sl = instSeller(rs, dp);
                return sl;
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
    public List<Seller> findByDepartment(Department dp){
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT seller.*,department.Name as DepName "
            + "FROM seller INNER JOIN department "
            + "ON seller.DepartmentId = department.Id "
            + "WHERE DepartmentId = ? "
            + "ORDER BY Name");

            pst.setInt(1, dp.getId());
            rs = pst.executeQuery();
            
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                
                if(dep == null){
                    dep = instDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                } 

                Seller sl = instSeller(rs, dep);
                list.add(sl);
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
    public List<Seller> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        try{
            pst = con.prepareStatement(
            "SELECT seller.*,department.Name as DepName "
            + "FROM seller INNER JOIN department "
            + "ON seller.DepartmentId = department.Id "
            + "ORDER BY Name");

            rs = pst.executeQuery();
            
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                
                if(dep == null){
                    dep = instDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                } 

                Seller sl = instSeller(rs, dep);
                list.add(sl);
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
    
    //instantiate
    private Department instDepartment(ResultSet rs) throws SQLException{
        Department dp = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
        return dp;
    }

    private Seller instSeller(ResultSet rs, Department dp) throws SQLException{
        Seller sl = new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"), rs.getDouble("BaseSalary"), dp);
        return sl;
    }
}

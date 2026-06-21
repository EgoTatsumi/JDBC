package com.dnd.dao;

import com.dnd.db.ConnectionManager;
import com.dnd.model.Company;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDao {

    public List<Company> findAll() {
        String sql = "SELECT id_company, title, setting FROM company ORDER BY id_company";
        List<Company> list = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Optional<Company> findById(int id) {
        String sql = "SELECT id_company, title, setting FROM company WHERE id_company = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Company save(Company c) {
        String sql = "INSERT INTO company (title, setting) VALUES (?, ?) RETURNING id_company";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getTitle());
            pstmt.setString(2, c.getSetting());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    c.setIdCompany(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public void update(Company c) {
        String sql = "UPDATE company SET title = ?, setting = ? WHERE id_company = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getTitle());
            pstmt.setString(2, c.getSetting());
            pstmt.setInt(3, c.getIdCompany());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM company WHERE id_company = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Company mapRow(ResultSet rs) throws SQLException {
        return new Company(
                rs.getInt("id_company"),
                rs.getString("title"),
                rs.getString("setting")
        );
    }
}
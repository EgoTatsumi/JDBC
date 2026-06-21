package com.dnd.dao;

import com.dnd.db.ConnectionManager;
import com.dnd.model.Monster;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MonsterDao {

    public List<Monster> findAll() {
        String sql = "SELECT name, strength, level FROM monster";
        List<Monster> list = new ArrayList<>();
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

    public Optional<Monster> findById(String name) {
        String sql = "SELECT name, strength, level FROM monster WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
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

    public void save(Monster m) {
        String sql = "INSERT INTO monster (name, strength, level) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getName());
            pstmt.setShort(2, m.getStrength());
            pstmt.setShort(3, m.getLevel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Monster m) {
        String sql = "UPDATE monster SET strength = ?, level = ? WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setShort(1, m.getStrength());
            pstmt.setShort(2, m.getLevel());
            pstmt.setString(3, m.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(String name) {
        String sql = "DELETE FROM monster WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Monster mapRow(ResultSet rs) throws SQLException {
        return new Monster(
                rs.getString("name"),
                rs.getShort("strength"),
                rs.getShort("level")
        );
    }
}
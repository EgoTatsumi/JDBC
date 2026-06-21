package com.dnd.dao;

import com.dnd.db.ConnectionManager;
import com.dnd.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDao {

    public List<Item> findAll() {
        String sql = "SELECT id_item, id_person_fk, weight, magic_properties FROM item ORDER BY id_item";
        List<Item> list = new ArrayList<>();
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

    public Optional<Item> findById(int id) {
        String sql = "SELECT id_item, id_person_fk, weight, magic_properties FROM item WHERE id_item = ?";
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

    public Item save(Item i) {
        String sql = "INSERT INTO item (id_person_fk, weight, magic_properties) VALUES (?, ?, ?) RETURNING id_item";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i.getIdPersonFk());
            pstmt.setShort(2, i.getWeight());
            pstmt.setString(3, i.getMagicProperties());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    i.setIdItem(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    public void update(Item i) {
        String sql = "UPDATE item SET id_person_fk = ?, weight = ?, magic_properties = ? WHERE id_item = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, i.getIdPersonFk());
            pstmt.setShort(2, i.getWeight());
            pstmt.setString(3, i.getMagicProperties());
            pstmt.setInt(4, i.getIdItem());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM item WHERE id_item = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Item mapRow(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("id_item"),
                rs.getInt("id_person_fk"),
                rs.getShort("weight"),
                rs.getString("magic_properties")
        );
    }
}
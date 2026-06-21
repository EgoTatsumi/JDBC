package com.dnd.dao;

import com.dnd.db.ConnectionManager;
import com.dnd.model.Person;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDao {

    public List<Person> findAll() {
        String sql = "SELECT id_person, id_class, id_race, level, name FROM person ORDER BY id_person";
        List<Person> list = new ArrayList<>();
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

    public Optional<Person> findById(int id) {
        String sql = "SELECT id_person, id_class, id_race, level, name FROM person WHERE id_person = ?";
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

    public Person save(Person p) {
        String sql = "INSERT INTO person (id_class, id_race, level, name) VALUES (?, ?, ?, ?) RETURNING id_person";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getIdClass());
            pstmt.setInt(2, p.getIdRace());
            pstmt.setShort(3, p.getLevel());
            pstmt.setString(4, p.getName());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    p.setIdPerson(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    public void update(Person p) {
        String sql = "UPDATE person SET id_class = ?, id_race = ?, level = ?, name = ? WHERE id_person = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getIdClass());
            pstmt.setInt(2, p.getIdRace());
            pstmt.setShort(3, p.getLevel());
            pstmt.setString(4, p.getName());
            pstmt.setInt(5, p.getIdPerson());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM person WHERE id_person = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Person mapRow(ResultSet rs) throws SQLException {
        return new Person(
                rs.getInt("id_person"),
                rs.getInt("id_class"),
                rs.getInt("id_race"),
                rs.getShort("level"),
                rs.getString("name")
        );
    }
}
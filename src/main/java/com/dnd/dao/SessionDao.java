package com.dnd.dao;

import com.dnd.db.ConnectionManager;
import com.dnd.model.Session;
import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionDao {

    public List<Session> findAll() {
        String sql = "SELECT id_session, name_company_fk, content, address, date FROM session ORDER BY id_session";
        List<Session> list = new ArrayList<>();
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

    public Optional<Session> findById(int id) {
        String sql = "SELECT id_session, name_company_fk, content, address, date FROM session WHERE id_session = ?";
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

    public Session save(Session s) {
        String sql = "INSERT INTO session (name_company_fk, content, address, date) VALUES (?, ?, ?, ?) RETURNING id_session";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.getNameCompanyFk());
            pstmt.setString(2, s.getContent());
            pstmt.setString(3, s.getAddress());
            pstmt.setObject(4, s.getDate());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    s.setIdSession(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    public void update(Session s) {
        String sql = "UPDATE session SET name_company_fk = ?, content = ?, address = ?, date = ? WHERE id_session = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.getNameCompanyFk());
            pstmt.setString(2, s.getContent());
            pstmt.setString(3, s.getAddress());
            pstmt.setObject(4, s.getDate());
            pstmt.setInt(5, s.getIdSession());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM session WHERE id_session = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Session mapRow(ResultSet rs) throws SQLException {
        return new Session(
                rs.getInt("id_session"),
                rs.getInt("name_company_fk"),
                rs.getString("content"),
                rs.getString("address"),
                rs.getObject("date", OffsetDateTime.class)
        );
    }
}
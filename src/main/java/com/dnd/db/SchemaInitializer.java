package com.dnd.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.stream.Collectors;

/**
 * Инициализация схемы БД D&D и заполнение тестовыми данными справочников.
 */
public class SchemaInitializer {

    private static final Logger log = LoggerFactory.getLogger(SchemaInitializer.class);

    public static void initialize() throws SQLException {
        log.info("Инициализация схемы D&D базы данных...");
        executeSqlFile("schema.sql");
        seedTestData();
        log.info("Схема D&D создана и заполнена базовыми справочниками");
    }

    private static void executeSqlFile(String fileName) throws SQLException {
        String sql;
        try (InputStream is = SchemaInitializer.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new RuntimeException("SQL-файл не найден: " + fileName);
            sql = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения SQL-файла: " + fileName, e);
        }

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Выполнен SQL-файл схемы: {}", fileName);
        }
    }

    private static void seedTestData() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                seedClasses(conn);
                seedRaces(conn);
                conn.commit();
                log.info("Стартовые игровые справочники успешно загружены");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private static void seedClasses(Connection conn) throws SQLException {
        String sql = "INSERT INTO character_class (class_name) VALUES (?) ON CONFLICT DO NOTHING";
        String[] classes = {"Воин", "Маг", "Плут", "Жрец", "Паладин", "Бард", "Следопыт"};
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String c : classes) {
                ps.setString(1, c);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void seedRaces(Connection conn) throws SQLException {
        String sql = "INSERT INTO race (race_name) VALUES (?) ON CONFLICT DO NOTHING";
        String[] races = {"Человек", "Эльф", "Дворф", "Полурослик", "Тифлинг", "Драконорожденный"};
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String r : races) {
                ps.setString(1, r);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
package com.dnd.service;

import com.dnd.db.ConnectionManager;
import java.sql.*;

/**
 * Примеры бизнес-запросов на чистом SQL через JDBC для D&D игрового центра.
 */
public class BusinessQueryService {

    // 1. Количество схваток по монстрам
    public void fightsByMonster() {
        printHeader("Количество схваток по монстрам");
        String sql = """
                SELECT name_monster_fk, COUNT(*), AVG(fight_status) 
                FROM fight 
                GROUP BY name_monster_fk 
                ORDER BY COUNT(*) DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-22s %-12s %-15s%n", "Монстр", "Схваток", "Ср. Статус");
            System.out.println("-".repeat(50));
            while (rs.next()) {
                System.out.printf("%-22s %-12d %-15.1f%n",
                        rs.getString(1), rs.getLong(2), rs.getDouble(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 2. Статистика уровней по расам персонажей
    public void raceLevelStatistics() {
        printHeader("Статистика уровней по расам");
        String sql = """
                SELECT r.race_name, COUNT(p.id_person), MIN(p.level), MAX(p.level), AVG(p.level) 
                FROM person p 
                JOIN race r ON p.id_race = r.id_race 
                GROUP BY r.race_name 
                ORDER BY COUNT(p.id_person) DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-16s %-10s %-8s %-8s %-10s%n", "Раса", "Героев", "Мин.Лвл", "Макс.Лвл", "Ср. Уровень");
            System.out.println("-".repeat(55));
            while (rs.next()) {
                System.out.printf("%-16s %-10d %-8d %-8d %-10.1f%n",
                        rs.getString(1), rs.getLong(2), rs.getShort(3), rs.getShort(4), rs.getDouble(5));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 3. Топ-3 самых высокоуровневых персонажей
    public void topHighLevelCharacters() {
        printHeader("Топ-3 сильнейших героев");
        String sql = """
                SELECT p.name, c.class_name, p.level 
                FROM person p 
                JOIN character_class c ON p.id_class = c.id_class 
                ORDER BY p.level DESC 
                LIMIT 3
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-20s %-15s %-10s%n", "Имя персонажа", "Класс", "Уровень");
            System.out.println("-".repeat(45));
            while (rs.next()) {
                System.out.printf("%-20s %-15s %-10d%n",
                        rs.getString(1), rs.getString(2), rs.getShort(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 4. Персонажи и количество их магических предметов
    public void charactersWithItemCount() {
        printHeader("Персонажи и количество предметов");
        String sql = """
                SELECT p.name, p.level, COUNT(i.id_item) 
                FROM person p 
                LEFT JOIN item i ON i.id_person_fk = p.id_person 
                GROUP BY p.id_person, p.name, p.level 
                ORDER BY COUNT(i.id_item) DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-22s %-12s %-10s%n", "Имя персонажа", "Уровень", "Предметов");
            System.out.println("-".repeat(45));
            while (rs.next()) {
                System.out.printf("%-22s %-12d %-10d%n",
                        rs.getString(1), rs.getShort(2), rs.getLong(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 5. Список запланированных сессий с деталями кампании
    public void sessionsWithDetails() {
        printHeader("Расписание сессий по кампаниям");
        String sql = """
                SELECT c.title, s.address, s.date, s.content 
                FROM session s 
                JOIN company c ON s.name_company_fk = c.id_company 
                ORDER BY s.date
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-25s %-20s %-25s%n", "Кампания", "Адрес проведения", "Дата сессии");
            System.out.println("-".repeat(70));
            while (rs.next()) {
                System.out.printf("%-25s %-20s %-25s%n",
                        rs.getString(1), rs.getString(2), rs.getTimestamp(3));
                System.out.printf("  > Сюжет: %s%n%n", rs.getString(4));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 6. Популярность классов персонажей в схватках
    public void classPopularityInFights() {
        printHeader("Участие классов в битвах");
        String sql = """
                SELECT cc.class_name, COUNT(f.id_fight) 
                FROM fight f 
                JOIN person p ON f.id_person_fk = p.id_person 
                JOIN character_class cc ON p.id_class = cc.id_class 
                GROUP BY cc.class_name 
                ORDER BY COUNT(f.id_fight) DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-20s %-10s%n", "Класс", "Схваток");
            System.out.println("-".repeat(30));
            while (rs.next()) {
                System.out.printf("%-20s %-10d%n", rs.getString(1), rs.getLong(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 7. Средняя сила монстров, напавших на сессиях
    public void avgMonsterStrengthBySession() {
        printHeader("Средняя сила монстров на сессиях");
        String sql = """
                SELECT am.id_session_fk, c.title, COUNT(am.name_monster_fk), AVG(m.strength) 
                FROM accounting_monsters am 
                JOIN session s ON am.id_session_fk = s.id_session 
                JOIN company c ON s.name_company_fk = c.id_company 
                JOIN monster m ON am.name_monster_fk = m.name 
                GROUP BY am.id_session_fk, c.title 
                ORDER BY am.id_session_fk
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-8s %-25s %-12s %-15s%n", "ID Сесс", "Кампания", "Видов", "Ср. Сила");
            System.out.println("-".repeat(60));
            while (rs.next()) {
                System.out.printf("%-8d %-25s %-12d %-15.1f%n",
                        rs.getInt(1), rs.getString(2), rs.getLong(3), rs.getDouble(4));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 8. Персонажи-одиночки (не участвующие ни в одной кампании)
    public void lonelyCharactersWithoutCompanies() {
        printHeader("Персонажи вне игровых кампаний");
        String sql = """
                SELECT p.name, cc.class_name 
                FROM person p 
                JOIN character_class cc ON p.id_class = cc.id_class 
                WHERE p.id_person NOT IN (SELECT id_person_fk FROM participation) 
                ORDER BY p.name
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-25s %-25s%n", "Имя героя", "Класс");
            System.out.println("-".repeat(50));
            boolean empty = true;
            while (rs.next()) {
                empty = false;
                System.out.printf("%-25s %-25s%n", rs.getString(1), rs.getString(2));
            }
            if (empty) {
                System.out.println("  (все персонажи задействованы в кампаниях)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 9. Список монстров, которых еще не задействовали на сессиях
    public void unusedMonstersForSessions() {
        printHeader("Незадействованные в сессиях монстры");
        String sql = """
                SELECT name, level, strength 
                FROM monster 
                WHERE name NOT IN (SELECT name_monster_fk FROM accounting_monsters) 
                ORDER BY level DESC
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-22s %-10s %-10s%n", "Имя монстра", "Уровень", "Сила");
            System.out.println("-".repeat(42));
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%-22s %-10d %-10d%n",
                        rs.getString(1), rs.getShort(2), rs.getShort(3));
            }
            System.out.printf("В резерве монстров: %d%n", count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    // 10. Общая статистика кампаний: название, количество сессий и участников
    public void companyStatistics() {
        printHeader("Общая статистика игровых кампаний");
        String sql = """
                SELECT c.title,
                       (SELECT COUNT(*) FROM session s WHERE s.name_company_fk = c.id_company),
                       (SELECT COUNT(*) FROM participation p WHERE p.title_company_fk = c.id_company)
                FROM company c
                ORDER BY c.title
                """;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.printf("%-30s %-10s %-10s%n", "Кампания", "Сессий", "Игроков");
            System.out.println("-".repeat(50));
            while (rs.next()) {
                System.out.printf("%-30s %-10d %-10d%n",
                        rs.getString(1), rs.getLong(2), rs.getLong(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        printDivider();
    }

    public void runQueries() {
        fightsByMonster();
        raceLevelStatistics();
        topHighLevelCharacters();
        charactersWithItemCount();
        sessionsWithDetails();
        classPopularityInFights();
        avgMonsterStrengthBySession();
        lonelyCharactersWithoutCompanies();
        unusedMonstersForSessions();
        companyStatistics();
    }

    private void printHeader(String title) {
        System.out.println("\n>>> " + title.toUpperCase());
    }

    private void printDivider() {
        System.out.println("=".repeat(60));
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}